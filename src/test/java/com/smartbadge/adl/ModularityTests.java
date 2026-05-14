//package com.smartbadge.adl;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.modulith.core.ApplicationModules;
//import org.springframework.modulith.docs.Documenter;
//
///**
// * Verifies Spring Modulith module structure and generates documentation.
// *
// * Run: ./mvnw test -Dtest=ModularityTests
// * Output: target/modulith-docs/
// */
//class ModularityTests {
//
//    private final ApplicationModules modules =
//            ApplicationModules.of(StaffManagementApplication.class);
//
//    @Test
//    void verifiesModularStructure() {
//        modules.verify();
//    }
//
//    @Test
//    void createModuleDocumentation() {
//        new Documenter(modules)
//                .writeDocumentation()
//                .writeIndividualModulesAsPlantUml();
//    }
//}
