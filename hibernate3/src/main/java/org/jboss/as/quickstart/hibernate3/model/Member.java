package org.jboss.as.quickstart.hibernate3.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.Digits;
import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;


@Entity
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "id"), name="MemberHibernate3")
public class Member implements Serializable {
   /** Default value included to remove warning. Remove or modify at will. **/
   private static final long serialVersionUID = 1L;

   @Id
   private Long id;

   @NotNull
   @Length(min = 1, max = 25)
   @Pattern(regex = "[A-Za-z ]*", message = "must contain only letters and spaces")
   private String name;

   @NotNull
   @NotEmpty
   @Email
   private String email;

   @NotNull
   @Length(min = 9, max = 12, message="Size must be between 9 and 12 digits")
   @Digits(fractionalDigits = 0, integerDigits = 12,message="Not allowed digit!")
   @Column(name = "phone_number")
   private String phoneNumber;
   
   private String address;


   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPhoneNumber() {
      return phoneNumber;
   }

   public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   public String getAddress() {
	return address;
   }

   public void setAddress(String address) {
	this.address = address;
   }
}
