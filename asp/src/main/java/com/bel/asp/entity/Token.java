package com.bel.asp.entity;





import com.bel.asp.enums.TokenType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {

	  @Id
	  @GeneratedValue
	  public Integer id;

	  @Column(unique = true)
	  public String token;

	  @Enumerated(EnumType.STRING)
	  public TokenType tokenType = TokenType.BEARER;

	  public boolean revoked;

	  public boolean expired;

	  @ManyToOne
	  @JsonIgnore
	  @JoinColumn(name = "user_id")
	  public User user;
	
}
