package com.dkha.dto.doorcontrol;

import lombok.Data;

@Data
public class PackageAuthorityDto {
    private String aeId;
    private int versionCode;

    private String packageName;

    private String versionName;
}
