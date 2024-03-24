package com.jia.ac.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jialunyin
 * @version 1.0
 */
@Data
public class UserRegisterRequest implements Serializable {


    private static final long serialVersionUID = 6199173023001009963L;

    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;
}
