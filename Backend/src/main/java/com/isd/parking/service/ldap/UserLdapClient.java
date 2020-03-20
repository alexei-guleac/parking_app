package com.isd.parking.service.ldap;

import com.isd.parking.models.UserLdap;
import com.isd.parking.repository.UserLdapRepository;
import com.isd.parking.security.PasswordEncoding.CustomBcryptPasswordEncoder;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.query.SearchScope;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import javax.naming.directory.*;
import java.time.LocalDateTime;
import java.util.List;

import static com.isd.parking.service.ldap.LdapAttributeMappers.*;
import static com.isd.parking.service.ldap.LdapConstants.*;
import static com.isd.parking.service.ldap.LdapFileUtils.deleteEntryFromLdifFile;
import static org.springframework.ldap.query.LdapQueryBuilder.query;


/**
 * UserLdap Service class for ldap storage repository
 * Contains methods for
 * authenticate user,
 * searchUser user by uid,
 * create new user entry in ldap repository,
 * modify existed user,
 * get all users from ldap storage
 */
@Service
@Slf4j
public class UserLdapClient {

    private final LdapTemplate ldapTemplate;

    private final UserLdapRepository userLdapRepository;

    private final CustomBcryptPasswordEncoder passwordEncoder;

    private final ColorConsoleOutput console;

    @Value("${spring.ldap.base}")
    private String ldapSearchBase;

    @Value("${ldap.passwordAttribute}")
    private String ldapPasswordAttribute;

    @Autowired
    public UserLdapClient(@Qualifier(value = "ldapTemplate") LdapTemplate ldapTemplate, UserLdapRepository userLdapRepository, CustomBcryptPasswordEncoder passwordEncoder, ColorConsoleOutput console) {
        this.ldapTemplate = ldapTemplate;
        this.userLdapRepository = userLdapRepository;
        this.passwordEncoder = passwordEncoder;
        this.console = console;
    }

    // --------- LDAp in memory template methods ---------

    /**
     * Method authenticates user with given credentials
     *
     * @param uid      - user name
     * @param password - user pass
     * @return - success or denied boolean status of user authentication
     */
    public Boolean authenticate(String uid, String password) {
        log.info(console.methodMsg(""));
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("uid", uid));

        return ldapTemplate.authenticate(ldapSearchBase, filter.encode(), passwordEncoder.encode(password));
    }

    /*private static javax.naming.Name bindDN(String uid) {
        @SuppressWarnings("deprecation")
        javax.naming.Name name = new DistinguishedName("uid=" + uid + ",ou=people");
        return name;
    }*/

    private Name buildDn(UserLdap user) {
        return LdapNameBuilder.newInstance()
            .add("ou", "people")
            .add("uid", user.getUid())
            .build();
    }

    private Name bindDnByUid(String uid) {
        return LdapNameBuilder.newInstance()
            .add("ou", "people")
            .add("uid", uid)
            .build();
    }

    public void createLdap(UserLdap user) {
        ldapTemplate.bind(buildDn(user), null, buildAttributes(user));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ldap.advance.example.UserRepositoryIntf#createUser(ldap.advance.example.UserLdap)
     */
    public boolean createUser(UserLdap user) {
        log.info(console.methodMsg(""));
        user.setUserRegistered();
        log.info(console.methodMsg("" + user));

        // save in-memory server
        ldapTemplate.bind(buildDn(user), null, buildAttributes(user));
        log.info(console.methodMsg(" before write" + user));
        // save to .ldif file
        userLdapRepository.save(user);

        return true;
    }

    public boolean updateUser(UserLdap user) {
        log.info(console.methodMsg(""));
        // upd in in-memory server
        ldapTemplate.rebind(buildDn(user), null, buildAttributes(user));
        // update in .ldif file
        userLdapRepository.update(user);

        return true;
    }

    public void updateLastName(UserLdap user) {
        log.info(console.methodMsg(""));
        Attribute attr = new BasicAttribute("sn", user.getUid());
        ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
        ldapTemplate.modifyAttributes(buildDn(user), new ModificationItem[]{item});
    }

    public boolean updateUserPassword(UserLdap user, String password) {
        log.info(console.methodMsg(""));
        // upd in in-memory server
        Attribute attr = new BasicAttribute(USER_PASSWORD_ATTRIBUTE, password);
        ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
        ldapTemplate.modifyAttributes(buildDn(user), new ModificationItem[]{item});

        // update in .ldif file
        userLdapRepository.updatePassword(user, password);

        return true;
    }


    public void modify(final String uid, final String password) {
        log.info(console.methodMsg(""));
        DirContextOperations context = ldapTemplate.lookupContext(bindDnByUid(uid));

        context.setAttributeValues(OBJECT_CLASS, personObjectClasses);
        context.setAttributeValue("cn", uid);
        context.setAttributeValue("sn", uid);
        context.setAttributeValue(ldapPasswordAttribute, passwordEncoder.encode(password));

        ldapTemplate.modifyAttributes(context);
    }

    public boolean deleteUser(UserLdap user) {
        log.info(console.methodMsg(""));
        // del from in-memory server
        ldapTemplate.unbind(buildDn(user));
        // delete from .ldif file
        deleteEntryFromLdifFile(user);

        return true;
    }

    /*
     * (non-Javadoc)
     * @see ldap.advance.example.UserRepositoryIntf#remove(java.lang.String)
     */
    public boolean deleteById(String uid) {
        log.info(console.methodMsg(""));
        ldapTemplate.unbind(bindDnByUid(uid));
        return true;
    }

    /**
     * Method search user by given uid
     *
     * @param uid - user name
     * @return - List of user names equals with given
     */

    public List<String> getUserNameById(final String uid) {
        log.info(console.methodMsg(""));
        return getAttributes(uid, "cn");
    }

    public String getAuthoritiesMembershipById(final String uid) {
        log.info(console.methodMsg(""));
        List<String> rolesList = ldapTemplate.search(
            ldapSearchBase, "uid=" + uid, new AuthoritiesAttributesMapper());
        if (rolesList != null && !rolesList.isEmpty()) {
            return rolesList.get(0);
        }
        return null;
    }

    public boolean searchUser(final String uid) {
        log.info(console.methodMsg(""));
        log.info(String.valueOf(getUserNameById(uid)));
        return !getUserNameById(uid).isEmpty();
    }

    public UserLdap findById(String uid) {
        log.info(console.methodMsg(""));
        UserLdap user = null;
        try {
            user = ldapTemplate.lookup(bindDnByUid(uid), new UserAttributesMapper());
        } catch (org.springframework.ldap.NameNotFoundException e) {
            // e.printStackTrace();
            return null;
        }
        return user;
    }

    public UserLdap findByDn(String dn) {
        log.info(console.methodMsg(""));
        UserLdap user = null;
        try {
            user = ldapTemplate.lookup(dn, new UserAttributesMapper());
        } catch (org.springframework.ldap.NameNotFoundException e) {
            // e.printStackTrace();
            return null;
        }
        return user;
    }

    /**
     * Get all users request method
     *
     * @return - list of all users
     */
    @SuppressWarnings("deprecation")
    public List<UserLdap> findAll() {
        log.info(console.methodMsg(""));
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        return ldapTemplate.search(DistinguishedName.EMPTY_PATH, "(objectclass=person)", controls, new UserAttributesMapper());
    }

    /*
     * (non-Javadoc)
     *
     * @see ldap.advance.example.UserRepositoryIntf#getUserDetails(java.lang.String)
     */
    public UserLdap getUserDetails(String uid) {
        log.info(console.methodMsg(""));
        List<UserLdap> list = ldapTemplate.search(query().base(ldapSearchBase).where("uid").is(uid), new UserAttributesMapper());
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see ldap.advance.example.UserRepositoryIntf#getUserDetail(java.lang.String)
     */
    public String getUserDetail(String uid) {
        log.info(console.methodMsg(""));
        List<String> results = ldapTemplate.search(query().base(ldapSearchBase).where("uid").is(uid), new MultipleAttributesMapper());
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return " UserDetails for " + uid + " not found .";
    }

    /*
     * (non-Javadoc)
     *
     * @see ldap.advance.example.UserRepositoryIntf#getAllUserNames()
     */
    public List<String> getAllUserNames() {
        log.info(console.methodMsg(""));
        LdapQuery query = query().base(ldapSearchBase);
        List<String> list = ldapTemplate.list(query.base());
        log.info("Users -> " + list);
        return ldapTemplate.search(query().base(ldapSearchBase).where(OBJECT_CLASS).is("person"), new SingleAttributesMapper());
    }

    public List<UserLdap> getAllUsers() {
        log.info(console.methodMsg(""));
        return ldapTemplate.search(query()
            .where(OBJECT_CLASS).is("person"), new UserAttributesMapper());
    }

    /* Custom search */

    public boolean searchUsersBySocialId(final String id, String social) {
        log.info(console.methodMsg("Id " + id + " social " + social));
        log.info(String.valueOf(getUsersBySocialId(id, social)));

        return !getUsersBySocialId(id, social).isEmpty();
    }

    public boolean searchUsersByEmail(final String email) {
        log.info(console.methodMsg("Email " + email));
        log.info(String.valueOf(getUsersByEmail(email)));

        return !getUsersByEmail(email).isEmpty();
    }

    public List<UserLdap> getUsersBySocialId(String id, String social) {
        LdapQuery query = getUserBySocialIdLdapQuery(id, social);

        return ldapTemplate.search(query, new UserAttributesMapper());
    }

    public UserLdap getUserBySocialId(String id, String social) {
        LdapQuery query = getUserBySocialIdLdapQuery(id, social);
        List<UserLdap> results = ldapTemplate.search(query, new UserAttributesMapper());
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    private LdapQuery getUserBySocialIdLdapQuery(String id, String social) {
        LdapQuery query = getBaseLdapQuery()
            //.attributes("cn")
            // .base(ldapSearchBase)
            .where(OBJECT_CLASS).is("person")
            .and(social + "id").like(id)
            .and("uid").isPresent();
        log.info(console.methodMsg("Query " + query));
        return query;
    }

    public List<UserLdap> getFullnameBySocialId(String id, String social) {
        LdapQuery query = getUserBySocialIdLdapQuery(id, social);

        return ldapTemplate.search(query, new UserAttributesMapperShort());
    }

    public List<UserLdap> getPersonNamesByLastName(String lastName) {

        LdapQuery query = getBaseLdapQuery()
            .attributes("cn")
            .where(OBJECT_CLASS).is("person")
            .and("sn").like(lastName)
            .and("uid").isPresent();

        return ldapTemplate.search(query, new UserAttributesMapperShort());
    }

    public List<UserLdap> getUsersByEmail(String email) {

        LdapQuery query = getBaseLdapQuery()
            .where(OBJECT_CLASS).is("person")
            .and("email").like(email)
            .and("uid").isPresent();

        return ldapTemplate.search(query, new UserAttributesMapper());
    }

    private LdapQueryBuilder getBaseLdapQuery() {
        return query()
            .searchScope(SearchScope.SUBTREE)
            .timeLimit(SEARCH_TIME_LIMIT_MS)
            .countLimit(3)
            .base(LdapUtils.emptyLdapName());
    }

    public UserLdap getUserByEmail(String email) {
        List<UserLdap> users = getUsersByEmail(email);
        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }

    public LocalDateTime getPasswordUpdateAt(String uid) {
        log.info(console.methodMsg(""));
        List<String> attributes = getAttributes(uid, "passwordUpdatedAt");
        log.info(console.methodMsg(String.valueOf(attributes)));
        if (!attributes.isEmpty()) {
            log.info(console.methodMsg("" + LocalDateTime.parse(attributes.get(0))));
            return LocalDateTime.parse(attributes.get(0));
        } else {
            return null;
        }
    }

    private List<String> getAttributes(String uid, String attributeName) {
        log.info(console.methodMsg(""));
        return ldapTemplate.search(
            ldapSearchBase,
            "uid=" + uid,
            (AttributesMapper<String>) attrs -> (String) attrs
                .get(attributeName)
                .get());
    }
}
