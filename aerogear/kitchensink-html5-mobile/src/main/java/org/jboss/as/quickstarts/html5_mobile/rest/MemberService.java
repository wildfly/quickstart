package org.jboss.as.quickstarts.html5_mobile.rest;

import java.util.*;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.*;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.as.quickstarts.html5_mobile.model.Member;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/members")
@RequestScoped
@Stateful
public class MemberService {
   @Inject
   private Logger log;

   @Inject
   private EntityManager em;

   @Inject
   private Event<Member> memberEventSrc;

   @Inject
   private Validator validator;

   @GET
   @Produces("text/xml")
   public List<Member> listAllMembers() {
      // Use @SupressWarnings to force IDE to ignore warnings about "genericizing" the results of
      // this query
      @SuppressWarnings("unchecked")

      // We recommend centralizing inline queries such as this one into @NamedQuery annotations on
      // the @Entity class
      // as described in the named query blueprint:
      // https://blueprints.dev.java.net/bpcatalog/ee5/persistence/namedquery.html
      final List<Member> results = em.createQuery("select m from Member m order by m.name").getResultList();
      return results;
   }

   @GET
   @Path("/json")
   @Produces(MediaType.APPLICATION_JSON)
   public List<Member> listAllMembersJSON() {
      @SuppressWarnings("unchecked")

      final List<Member> results = em.createQuery("select m from Member m order by m.name").getResultList();
      return results;
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("text/xml")
   public Member lookupMemberById(@PathParam("id") long id) {
      return em.find(Member.class, id);
   }

   @GET
   @Path("/{id:[0-9][0-9]*}/json")
   @Produces(MediaType.APPLICATION_JSON)
   public Member lookupMemberByIdJSON(@PathParam("id") long id) {
      return em.find(Member.class, id);
   }

   @POST
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   @Produces(MediaType.APPLICATION_JSON)
   public Response createMember(@FormParam("name") String name, @FormParam("email") String email, @FormParam("phoneNumber") String phone) {
      Response.ResponseBuilder builder = null;

      //Create a new member class from fields
      Member member = new Member();
      member.setName(name);
      member.setEmail(email);
      member.setPhoneNumber(phone);

      try {
         //Validates member using bean validation
         validateMember(member);

         //Register the member
         log.info("Registering " + member.getName());
         em.persist(member);

         //Trigger the creation event
         memberEventSrc.fire(member);

         //Create an "ok" response
         builder = Response.ok();
      } catch (ConstraintViolationException ce) {
         //Handle bean validation issues
         builder = createViolationResponse(ce.getConstraintViolations());
      } catch (ValidationException e) {
         //Handle the unique constrain violation
         Map<String, String> responseObj = new HashMap<String, String>();
         responseObj.put("email","Email taken");
         builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
      }

      return builder.build();
   }

   /**
    * <p>Validates the given Member variable and throws validation exceptions based on the type of error.
    * If the error is standard bean validation errors then it will throw a ConstraintValidationException
    * with the set of the constraints violated.</p>
    * <p>If the error is caused because an existing member with the same email is registered it throws a regular
    * validation exception so that it can be interpreted separately.</p>
    * @param member Member to be validated
    * @throws ConstraintViolationException If Bean Validation errors exist
    * @throws ValidationException If member with the same email already exists
    */
   private void validateMember(Member member) throws ConstraintViolationException, ValidationException{
      //Create a bean validator and check for issues.
      Set<ConstraintViolation<Member>> violations = validator.validate(member);

      if (!violations.isEmpty()) {
         throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
      }

      //Check the uniqueness of the email address
      if (emailAlreadyExists(member.getEmail())) {
         throw new ValidationException("Unique Email Violation");
      }
   }

   /**
    * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message.
    * This can then be used by clients to show violations.
    *
    * @param violations A set of violations that needs to be reported
    * @return JAX-RS response containing all violations
    */
   private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
      log.fine("Validation completed. violations found: " + violations.size());

      Response.ResponseBuilder builder = null;
      Map<String, String> responseObj = new HashMap<String, String>();

      for (ConstraintViolation<?> violation : violations) {
         responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
      }

      return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
   }

   /**
    * Checks if a member with the same email address is already registered.  This is the only way to
    * easily capture the "@UniqueConstraint(columnNames = "email")" constraint from the Member class.
    *
    * @param email The email to check
    * @return True if the email already exists, and false otherwise
    */
   public boolean emailAlreadyExists(String email) {
      Query checkEmailExists = em.createQuery(" SELECT COUNT(b.email) FROM Member b WHERE b.email=:emailparam");
      checkEmailExists.setParameter("emailparam", email);
      long matchCounter = 0;
      matchCounter = (Long) checkEmailExists.getSingleResult();
      if (matchCounter > 0) {
         return true;
      }
      return false;
   }
}
