package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.AssertTrue;

@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class EasyUITree {
    private Long id;
    private String text;
    private String state;
}
