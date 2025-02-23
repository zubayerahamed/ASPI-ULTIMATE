package com.zayaanit.aspi.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImadjdetailPK implements Serializable {
    
    private Integer zid;
    private Integer xadjnum;
    private Integer xrow;
}
