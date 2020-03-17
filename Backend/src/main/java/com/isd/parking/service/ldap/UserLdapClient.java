package com.isd.parking.service.ldap;

import com.isd.parking.models.User;
import com.isd.parking.security.PasswordEncoding;
import com.isd.parking.utils.ColorConsoleOutput;
import com.isd.parking.utils.MyStringUtils;
import com.unboundid.ldap.sdk.DN;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldif.LDIFWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.SearchScope;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.LdapName;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.springframework.ldap.query.LdapQueryBuilder.query;


/**
 * User Service class for ldap storage repository
 * Contains methods for
 * authenticate user,
 * searchUser user by username,
 * create new user entry in ldap repository,
 * modify existed user,
 * get all users from ldap storage
 */
@Service
@Slf4j
public class UserLdapClient {

    @Value("${spring.ldap.base}")
    private String ldapSearchBase;

    @Value("${ldap.passwordAttribute}")
    private String ldapPasswordAttribute;

    // value error create
    private final String ldapBaseDn = "dc=isd,dc=com";

    private final String[] objectClasses = {"top", "person", "organizationalPerson", "inetOrgPerson"};

    private static final Integer THREE_SECONDS = 3000;

    private final LdapTemplate ldapTemplate;

    private final LdapName baseLdapPath = LdapUtils.newLdapName(ldapBaseDn);

    private final PasswordEncoding.CustomPasswordEncoder passwordEncoder;

    private final ColorConsoleOutput console;

    @Autowired
    public UserLdapClient(@Qualifier(value = "ldapTemplate") LdapTemplate ldapTemplate, PasswordEncoding.CustomPasswordEncoder passwordEncoder, ColorConsoleOutput console) {
        this.ldapTemplate = ldapTemplate;
        this.passwordEncoder = passwordEncoder;
        this.console = console;
    }

    // --------- LDAp in memory template methods ---------

    /**
     * Method authenticates user with given credentials
     *
     * @param username - user name
     * @param password - user pass
     * @return - success or denied boolean status of user authentication
     */
    public Boolean authenticate(String username, String password) {
        log.info(console.methodMsg(""));
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("uid", username));

        return ldapTemplate.authenticate(ldapSearchBase, filter.encode(), passwordEncoder.encode(password));
    }

    public static javax.naming.Name bindDN(String _x) {
        @SuppressWarnings("deprecation")
        javax.naming.Name name = new DistinguishedName("uid=" + _x + ",ou=people");
        return name;
    }

    private Name buildDn(User user) {
        return LdapNameBuilder.newInstance(ldapBaseDn)
                .add("ou", "people")
                .add("uid", user.getId())
                .build();
    }

    private Name bindDnByUid(String uid) {
        return LdapNameBuilder.newInstance()
                .add("ou", "people")
                .add("uid", uid)
                .build();
    }

    public void createLdap(User user) {
        ldapTemplate.bind(buildDn(user), null, buildAttributes(user));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ldap.advance.example.UserRepositoryIntf#createUser(ldap.advance.example.User)
     */
    public boolean createUser(User user) {
        log.info(console.methodMsg(""));
        Attributes userAttributes = mapUserAttributes(user);

        // save in-memory server
        ldapTemplate.bind(bindDN(user.getUsername()), null, userAttributes);

        // write to .ldif file
        writeEntryToLdifFile(user);

        return true;
    }

    private void writeEntryToLdifFile(User user) {
        Entry entry;
        try {
            entry = new Entry(new DN("uid=" + user.getUsername() + ",ou=people,dc=isd,dc=com"));
            mapUserToEntry(user, entry);
            log.info(console.methodMsg("Entry " + entry));

            // Write all of the matching entries to LDIF.
            LDIFWriter ldifWriter;
            try {
                String ldifFilePath = "N:\\Programming\\IFC\\Diplome\\Diplome\\Back\\Backend\\src\\main\\resources\\ldap-server.ldif";
                //private final String ldifFilePath = "classpath:ldap-server.ldif";
                //private final String ldifFilePath = FileUtils.readPropertiesFiles("ldap-server.ldif");
                /*@Value(value = ldifFilePath)
                private Resource companiesXml;*/
                // private final String ldifFilePath = FileUtils.readPropertiesFiles("ldap-server.ldif");

                ldifWriter = new LDIFWriter(new FileOutputStream(new File(ldifFilePath), true));
                ldifWriter.writeEntry(entry);
                ldifWriter.close();
            } catch (IOException e) {
                //throw new LdapMappingException("Error writing to file, try again", e);
            }
        } catch (LDAPException e) {
            e.printStackTrace();
        }
    }

    private void mapUserToEntry(User user, Entry entry) {
        entry.addAttribute("objectClass", objectClasses);
        entry.addAttribute("cn", user.getFullname());
        entry.addAttribute("sn", user.getLastname());
        entry.addAttribute("email", user.getEmail());
        entry.addAttribute("creationDate", String.valueOf(new Date(System.currentTimeMillis())));
        entry.addAttribute("uid", user.getUsername());
        entry.addAttribute(ldapPasswordAttribute, user.getPassword().getBytes());
    }

    private Attributes mapUserAttributes(User user) {
        Attribute objectClass = new BasicAttribute("objectClass");
        {
            for (String objClass : objectClasses) {
                objectClass.add(objClass);
            }
        }
        Attributes userAttributes = new BasicAttributes();
        userAttributes.put(objectClass);
        userAttributes.put("uid", user.getUsername());
        userAttributes.put("cn", user.getFullname());
        userAttributes.put("sn", user.getLastname());
        //userAttributes.put("email", user.getEmail());
        userAttributes.put(ldapPasswordAttribute, user.getPassword().getBytes());

        return userAttributes;
    }

    public void update(User user) {
        log.info(console.methodMsg(""));
        ldapTemplate.rebind(buildDn(user), null, buildAttributes(user));
    }

    public void updateLastName(User user) {
        log.info(console.methodMsg(""));
        Attribute attr = new BasicAttribute("sn", user.getLastname());
        ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
        ldapTemplate.modifyAttributes(buildDn(user), new ModificationItem[]{item});
    }

    public void updateUserPassword(String username, String password) {
        log.info(console.methodMsg(""));
        Attribute attr = new BasicAttribute("userPassword", password);
        ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
        ldapTemplate.modifyAttributes(bindDnByUid(username), new ModificationItem[]{item});
    }

    public void modify(final String username, final String password) {
        log.info(console.methodMsg(""));
        DirContextOperations context = ldapTemplate.lookupContext(bindDnByUid(username));

        context.setAttributeValues("objectclass", new String[]{"top", "person", "organizationalPerson", "inetOrgPerson"});
        context.setAttributeValue("cn", username);
        context.setAttributeValue("sn", username);
        context.setAttributeValue(ldapPasswordAttribute, passwordEncoder.encode(password));

        ldapTemplate.modifyAttributes(context);
    }

    private Attributes buildAttributes(User user) {
        Attributes attrs = new BasicAttributes();
        BasicAttribute ocAttr = new BasicAttribute("objectclass");
        ocAttr.add("top");
        ocAttr.add("person");
        ocAttr.add("inetOrgPerson");
        ocAttr.add("organizationalPerson");

        attrs.put(ocAttr);
        attrs.put("ou", "people");
        attrs.put("uid", user.getId());
        attrs.put("cn", user.getFullname());
        attrs.put("sn", user.getLastname());
        return attrs;
    }

    public void delete(User user) {
        log.info(console.methodMsg(""));
        ldapTemplate.unbind(buildDn(user));
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
     * Method search user by given username
     *
     * @param username - user name
     * @return - List of user names equals with given
     */

    public List<String> getUserNameById(final String username) {
        log.info(console.methodMsg(""));
        return ldapTemplate.search(
                ldapSearchBase,
                "uid=" + username,
                (AttributesMapper<String>) attrs -> (String) attrs
                        .get("cn")
                        .get());
    }

    public List<String> getAuthoritiesMembershipById(final String username) {

        List<String> l = new ArrayList<>();
        log.info(console.methodMsg(""));

        return ldapTemplate.search(
                ldapSearchBase,
                "uid=" + username,
                (AttributesMapper<String>) attrs -> {
                    Attribute attr = attrs.get("memberOf");

                    for (int i = 0; i < attr.size(); i++) {
                        String s = (String) attr.get(i);
                        String[] str = s.split(",");
                        str = str[0].split("=");
                        l.add("ROLE_" + str[1]);
                    }
                    return new MyStringUtils().collectionToString(l);
                });
    }


    public boolean searchUser(final String username) {
        log.info(console.methodMsg(""));
        log.info(String.valueOf(getUserNameById(username)));
        return !getUserNameById(username).isEmpty();
    }

    public User findById(String uid) {
        return ldapTemplate.lookup(bindDnByUid(uid), new UserContextMapper());
    }

    public User findByDn(String dn) {
        log.info(console.methodMsg(""));
        return ldapTemplate.lookup(dn, new UserAttributesMapper());
    }

    /**
     * Get all users request method
     *
     * @return - list of all users
     */
    @SuppressWarnings("deprecation")
    public List<User> findAll() {
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
    public User getUserDetails(String userName) {
        log.info(console.methodMsg(""));
        List<User> list = ldapTemplate.search(query().base(ldapSearchBase).where("uid").is(userName), new UserAttributesMapper());
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
    public String getUserDetail(String userName) {
        log.info(console.methodMsg(""));
        List<String> results = ldapTemplate.search(query().base(ldapSearchBase).where("uid").is(userName), new MultipleAttributesMapper());
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return " userDetails for " + userName + " not found .";
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
        return ldapTemplate.search(query().base(ldapSearchBase).where("objectClass").is("person"), new SingleAttributesMapper());
    }

    public List<User> getAllUsers() {
        log.info(console.methodMsg(""));
        return ldapTemplate.search(query()
                .where("objectclass").is("person"), new UserAttributesMapper());
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

    public List<User> getUsersBySocialId(String id, String social) {
        LdapQuery query = getUserBySocialIdLdapQuery(id, social);

        return ldapTemplate.search(query, new UserAttributesMapper());
    }

    public User getUserBySocialId(String id, String social) {
        LdapQuery query = getUserBySocialIdLdapQuery(id, social);

        return ldapTemplate.search(query, new UserAttributesMapper()).get(0);
    }

    public List<User> getFullnameBySocialId(String id, String social) {
        LdapQuery query = getUserBySocialIdLdapQuery(id, social);

        return ldapTemplate.search(query, new UserAttributesMapperShort());
    }

    private LdapQuery getUserBySocialIdLdapQuery(String id, String social) {
        LdapQuery query = query()
                .searchScope(SearchScope.SUBTREE)
                .timeLimit(THREE_SECONDS)
                .countLimit(3)
                //.attributes("cn")
                .base(ldapSearchBase)
                .where("objectclass").is("person")
                .and(social + "id").like(id)
                .and("uid").isPresent();
        log.info(console.methodMsg("Query " + query));
        return query;
    }

    public List<User> getPersonNamesByLastName(String lastName) {

        LdapQuery query = query()
                .searchScope(SearchScope.SUBTREE)
                .timeLimit(THREE_SECONDS)
                .countLimit(3)
                .attributes("cn")
                .base(LdapUtils.emptyLdapName())
                .where("objectclass").is("person")
                .and("sn").like(lastName)
                .and("uid").isPresent();

        return ldapTemplate.search(query, new UserAttributesMapperShort());
    }

    public List<User> getUsersByEmail(String email) {

        LdapQuery query = query()
                .searchScope(SearchScope.SUBTREE)
                .timeLimit(THREE_SECONDS)
                .countLimit(3)
                .base(LdapUtils.emptyLdapName())
                .where("objectclass").is("person")
                .and("email").like(email)
                .and("uid").isPresent();

        return ldapTemplate.search(query, new UserAttributesMapper());
    }

    public User getUserByEmail(String email) {
        List<User> users = getUsersByEmail(email);
        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }

    /*public List<User> getPersonNamesByLastName2(String lastName) {

        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sc.setTimeLimit(THREE_SECONDS);
        sc.setCountLimit(3);
        sc.setReturningAttributes(new String[]{"cn"});

        String filter = "(&(objectclass=person)(sn=" + lastName + "))";
        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter, sc, new UserAttributesMapperShort());
    }

    public List<User> getPersonNamesByLastName3(String lastName) {

        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sc.setTimeLimit(THREE_SECONDS);
        sc.setCountLimit(10);
        sc.setReturningAttributes(new String[]{"cn"});

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person"));
        filter.and(new EqualsFilter("sn", lastName));

        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter.encode(), sc, new UserAttributesMapperShort());
    }*/

    @PostConstruct
    private void readFilename() throws FileNotFoundException {

        String ldifFilePath = "ldap-server.ldif";
        String ldifFilePath2 = "classpath:ldap-server.ldif";

        File f = new File(ldifFilePath);
        log.info("File Found : " + f.exists());
        log.info("File name : " + f.getName());
        log.info("File path : " + f.getPath());

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File ldifFile = new File(Objects.requireNonNull(classLoader.getResource(ldifFilePath)).getFile());
        log.info("File Found : " + ldifFile.exists());
        log.info("File name : " + ldifFile.getName());
        log.info("File path : " + ldifFile.getPath());

        ldifFile = ResourceUtils.getFile(ldifFilePath2);
        //File is found
        log.info("File Found : " + ldifFile.exists());
        log.info("File name : " + ldifFile.getName());
        log.info("File path : " + ldifFile.getPath());
    }

    /**
     * Custom user attributes mapper, maps the attributes to the person POJO
     */
    private class UserAttributesMapperShort implements AttributesMapper<User> {
        @Override
        public User mapFromAttributes(Attributes attrs) throws NamingException {
            User user = new User();
            user.setFullname((String) attrs.get("cn").get());

            Attribute sn = attrs.get("sn");
            if (sn != null) {
                user.setLastname((String) sn.get());
            }
            return user;
        }
    }


    /**
     * This class is responsible to prepare User object after ldap search.
     *
     * @author
     */
    private static class UserAttributesMapper implements AttributesMapper<User> {

        @Override
        public User mapFromAttributes(Attributes attributes) throws NamingException {
            User user;
            if (attributes == null) {
                return null;
            }
            user = new User();
            user.setUsername(attributes.get("uid").get().toString());

            if (attributes.get("userPassword") != null) {
                String userPassword;
                userPassword = new String((byte[]) attributes.get("userPassword").get(), StandardCharsets.UTF_8);
                user.setPassword(userPassword);
            }
            if (attributes.get("email") != null) {
                user.setEmail(attributes.get("email").get().toString());
            }
            if (attributes.get("cn") != null) {
                user.setFullname(attributes.get("cn").get().toString());
            }
            if (attributes.get("sn") != null) {
                user.setLastname(attributes.get("sn").get().toString());
            }
            return user;
        }
    }

    private static class UserContextMapper extends AbstractContextMapper<User> {
        public User doMapFromContext(DirContextOperations context) {
            User user = new User();
            user.setFullname(context.getStringAttribute("cn"));
            user.setLastname(context.getStringAttribute("sn"));
            user.setUsername(context.getStringAttribute("uid"));

            return user;
        }
    }

    /**
     * This class is responsible to print only cn .
     *
     * @author
     */
    private static class SingleAttributesMapper implements AttributesMapper<String> {

        @Override
        public String mapFromAttributes(Attributes attrs) {
            Attribute cn = attrs.get("cn");
            return cn.toString();
        }
    }

    /**
     * This class is responsible to print all the content in string format.
     *
     * @author
     */
    private static class MultipleAttributesMapper implements AttributesMapper<String> {

        @Override
        public String mapFromAttributes(Attributes attrs) throws NamingException {
            NamingEnumeration<? extends Attribute> all = attrs.getAll();
            StringBuilder result = new StringBuilder();
            result.append("\n Result { \n");
            while (all.hasMore()) {
                Attribute id = all.next();
                result.append(" \t |_  #").append(id.getID()).append("= [ ").append(id.get()).append(" ]  \n");
                log.info(id.getID() + "\t | " + id.get());
            }
            result.append("\n } ");
            return result.toString();
        }
    }
}
