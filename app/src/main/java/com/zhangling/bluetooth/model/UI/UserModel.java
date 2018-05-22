package com.zhangling.bluetooth.model.UI;

import java.lang.reflect.Array;

public class UserModel {

    private long id;
    private String name;

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    private int departmentId;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    private int groupId;

    public int getParentDepartmentId() {
        return parentDepartmentId;
    }

    public void setParentDepartmentId(int parentDepartmentId) {
        this.parentDepartmentId = parentDepartmentId;
    }

    private int parentDepartmentId;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    private String nickName;
    private String password;


    private long uid;
    private String account;
    private int state;
    private int roleId;
    private String createdAt;
    private String updatedAt;
    private long phone;
    private String avatar;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public PermissionModel[] getMemberMenu() {
        return memberMenu;
    }

    public void setMemberMenu(PermissionModel[] memberMenu) {
        this.memberMenu = memberMenu;
    }

    private PermissionModel[] memberMenu;

    public class PermissionModel{
        private int menuCode;
        private String menuIcon;
        private int menuId;

        public int getMenuCode() {
            return menuCode;
        }

        public void setMenuCode(int menuCode) {
            this.menuCode = menuCode;
        }

        public String getMenuIcon() {
            return menuIcon;
        }

        public void setMenuIcon(String menuIcon) {
            this.menuIcon = menuIcon;
        }

        public int getMenuId() {
            return menuId;
        }

        public void setMenuId(int menuId) {
            this.menuId = menuId;
        }

        public String getMenuName() {
            return menuName;
        }

        public void setMenuName(String menuName) {
            this.menuName = menuName;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public String getMenuUrl() {
            return menuUrl;
        }

        public void setMenuUrl(String menuUrl) {
            this.menuUrl = menuUrl;
        }

        private String menuName;
        private int parentId;
        private String menuUrl;
    }
    public class DepartmentModel {
        private int id;
        private String name;
        private int ownerId;
        private String ownerName;
        private String desc;
        private int parentId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(int ownerId) {
            this.ownerId = ownerId;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public void setOwnerName(String ownerName) {
            this.ownerName = ownerName;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public int getAddTime() {
            return addTime;
        }

        public void setAddTime(int addTime) {
            this.addTime = addTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getOwnerPhone() {
            return ownerPhone;
        }

        public void setOwnerPhone(int ownerPhone) {
            this.ownerPhone = ownerPhone;
        }

        public Object getChildren() {
            return children;
        }

        public void setChildren(Object children) {
            this.children = children;
        }

        public Object getMembers() {
            return members;
        }

        public void setMembers(Object members) {
            this.members = members;
        }

        private int addTime;
        private int status;
        private int ownerPhone;
        private Object children;
        private Object members;
    }
}
