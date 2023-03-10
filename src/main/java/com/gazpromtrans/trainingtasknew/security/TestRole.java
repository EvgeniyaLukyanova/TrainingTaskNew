package com.gazpromtrans.trainingtasknew.security;

import io.jmix.security.role.annotation.RowLevelRole;

@RowLevelRole(name = "Test", code = "test")
public interface TestRole {
}