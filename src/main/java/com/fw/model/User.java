package com.fw.model;

import lombok.*;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Alias("User")
public class User implements Serializable {
    @NotBlank
    private int id;
    private String usernm;
    private String password;
    private String email;
    private String phone;
    private Date created_at;
    private Date updated_at;

}
