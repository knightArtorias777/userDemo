package com.jia.ac.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jialunyin
 * @version 1.0
 */
@Data
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = -7334290789472156704L;

    private String userAccount;
    private String userPassword;

}
