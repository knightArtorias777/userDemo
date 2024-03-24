package com.jia.ac.BusinessException;

import com.jia.ac.common.ErrorCode;
import lombok.Getter;



/**
 * @author jialunyin
 * @version 1.0
 */
@Getter
public class BusinessException extends RuntimeException {



    private static final long serialVersionUID = -6955056275604936855L;
    final private int code;
    final private String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }
}
