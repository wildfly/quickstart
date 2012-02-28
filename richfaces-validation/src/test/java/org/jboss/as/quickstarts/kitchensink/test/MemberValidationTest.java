package org.jboss.as.quickstarts.kitchensink.test;

import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Simple tests for Bean Validator. Arquillian deploys an WAR archive to the application server, which constructs Validator
 * object.
 * 
 * This object is injected into the tests so user can verify the validators are working. This example does not touch validation
 * on database layer, e.g. it is not validating uniqueness constraint for email field.
 * 
 * 
 * @author <a href="kpiwko@redhat.com>Karel Piwko</a>
 * 
 */
@RunWith(Arquillian.class)
public class MemberValidationTest {

    /**
     * Constructs a deployment archive
     * 
     * @return the deployment archive
     */
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addClasses(Member.class)
        // enable JPA
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                // add sample data
                .addAsResource("import.sql")
                // enable CDI
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    // Get configured validator directly from JBoss AS 7 environment
    @Inject
    Validator validator;

    /**
     * Tests an empty member registration, e.g. violation of:
     * 
     * <ul>
     * <li>@NotNull</li>
     * <li>@NotNull</li>
     * <li>@Email</li>
     * <li>@Size</li>
     * </ul>
     */
    @Test
    public void testRegisterEmptyMember() {

        Member member = new Member();
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        Assert.assertEquals("Four violations were expected", 3, violations.size());
    }

    /**
     * Tests an valid member registration
     */
    @Test
    public void testRegisterValidMember() {
        Set<ConstraintViolation<Member>> violations = validator.validate(createValidMember());

        Assert.assertEquals("No violations were found", 0, violations.size());
    }

    /**
     * Tests {@code @Pattern} constraint
     */
    @Test
    public void testNameViolation() {
        Member member = createValidMember();
        member.setName("Joe Doe-Dah");
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        Assert.assertEquals("One violation was found", 1, violations.size());
        Assert.assertEquals("Name was invalid", "must contain only letters and spaces", violations.iterator().next()
                .getMessage());
    }

    /**
     * Tests {@code @Email} constraint
     */
    @Test
    public void testEmailViolation() {
        Member member = createValidMember();
        member.setEmail("invalid-email");
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        Assert.assertEquals("One violation was found", 1, violations.size());
        Assert.assertEquals("Email was invalid", "not a well-formed email address", violations.iterator().next().getMessage());
    }

    /**
     * Tests {@code @Size} constraint
     */
    @Test
    public void testPhoneViolation() {
        Member member = createValidMember();
        member.setPhoneNumber("123");
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        Assert.assertEquals("One violation was found", 1, violations.size());
        Assert.assertEquals("Phone number was invalid", "Phone number must be 10-12 digits", violations.iterator().next()
                .getMessage());
    }

    private Member createValidMember() {
        Member member = new Member();
        member.setEmail("jdoe@test.org");
        member.setName("John Doe");
        member.setPhoneNumber("1234567890");
        return member;
    }

}
