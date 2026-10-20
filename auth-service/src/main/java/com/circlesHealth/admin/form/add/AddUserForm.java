package com.circlesHealth.admin.form.add;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserForm {

    private Integer id;
    private String name;
    private String description;
}
