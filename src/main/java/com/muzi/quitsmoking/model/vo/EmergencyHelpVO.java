package com.muzi.quitsmoking.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 紧急求助VO
 */
@Data
public class EmergencyHelpVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 紧急求助标题
     */
    private String title;

    /**
     * 紧急求助描述
     */
    private String description;

    /**
     * 紧急求助方法列表
     */
    private List<EmergencyMethod> methods;

    /**
     * 紧急联系人
     */
    private List<Contact> contacts;

    @Data
    public static class EmergencyMethod {
        /**
         * 方法ID
         */
        private Long id;

        /**
         * 方法名称
         */
        private String name;

        /**
         * 方法描述
         */
        private String description;

        /**
         * 方法步骤
         */
        private String steps;

        /**
         * 方法图标
         */
        private String icon;
    }

    @Data
    public static class Contact {
        /**
         * 联系人ID
         */
        private Long id;

        /**
         * 联系人名称
         */
        private String name;

        /**
         * 联系人电话
         */
        private String phone;

        /**
         * 联系人描述
         */
        private String description;
    }
} 