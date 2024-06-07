package com.fw.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest implements Serializable {

    @NotBlank(message = "First name can not be blank")
    private String userName;
    private Integer id;
    private String password;

    private String phone;
    private String email;
    private Date created_at;
    private Date updated_at;

//    private UserStatus status;
//    private Set<RoleDTO> role;

}
