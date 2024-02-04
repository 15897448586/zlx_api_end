package com.zlx.springbootinit.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zlx
 */
@Data
public class IdRequest implements Serializable {
    private Long id;

    private static final long serialVersionUID = 1L;
}
