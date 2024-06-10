package com.fw.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class SigupRequest {
    @NotBlank
    @Size(min = 4, max = 16)
    private String userName;

    @NotBlank
    @Size(max = 50)
    private String email;

    @NotBlank
    @Size(min = 4, max = 16)
    private String password;

    private Set<String> roles;
}
