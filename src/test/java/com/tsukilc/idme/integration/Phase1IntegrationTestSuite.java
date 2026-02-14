package com.tsukilc.idme.integration;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Phase 1 集成测试套件
 * 运行所有 Phase 1 的接口测试
 *
 * 运行方式：
 * 1. 在 IDE 中右键点击此类，选择 "Run"
 * 2. 或使用 Maven 命令：mvn test -Dtest=Phase1IntegrationTestSuite
 */
@Suite
@SuiteDisplayName("Phase 1 集成测试套件")
@SelectClasses({
    PartnerContactControllerIntegrationTest.class,
    BusinessPartnerControllerIntegrationTest.class,
    DepartmentControllerIntegrationTest.class
})
public class Phase1IntegrationTestSuite {
    // 测试套件类，无需实现代码
}
