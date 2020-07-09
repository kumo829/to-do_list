package com.javatutoriales.todo.userservice.repository;

import com.javatutoriales.todo.users.model.Role;
import com.javatutoriales.todo.userservice.repository.mongo.MongoDataFile;
import com.javatutoriales.todo.userservice.repository.mongo.MongoSpringExtension;
import com.mongodb.client.MongoClient;
import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.*;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
@ActiveProfiles("test")
@ExtendWith(MongoSpringExtension.class)
public class RoleRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RoleRepository roleRepository;

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

//    private static MongodExecutable mongodExecutable = null;
//
//    @AfterEach
//    void clean() {
//        mongodExecutable.stop();
//    }
//
//    @BeforeEach
//    void setup() throws Exception {
//        String ip = "localhost";
//        int port = 27017;
//
//        IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
//                .net(new Net(ip, port, Network.localhostIsIPv6()))
//                .build();
//
//        MongodStarter starter = MongodStarter.getDefaultInstance();
//        mongodExecutable = starter.prepare(mongodConfig);
//        mongodExecutable.start();
//        mongoTemplate = new MongoTemplate(new MongoClient(ip, port), "test");
//    }
//
//    @AfterAll
//    public static void tearDown() {
//        if (mongodExecutable != null)
//            mongodExecutable.stop();
//    }

    @Test
    @DisplayName("Save Role - SUCCESS")
    public void whenSaveRoleUniqueName_thenRoleOnMongoDB() {
        Role role = Role.builder().name("Admin").version(1).build();

        Role savedRole = roleRepository.save(role);

        Optional<Role> loadedRole = roleRepository.findById(savedRole.getId());

        assertThat(loadedRole).isPresent();
        loadedRole.ifPresent(r -> {
            assertThat(r.getName()).isEqualTo(role.getName());
            assertThat(r.getVersion()).isEqualTo(1);
        });

        mongoTemplate.remove(savedRole);
    }

    @Test
    @DisplayName("Get all Roles - SUCCESS")
    @MongoDataFile(value = "roles.json", classType = Role.class, collectionName = "roles")
    public void whenGetAllRoles_thenRolesListHasTwoElements() {
        List<Role> roleList = roleRepository.findAll();

        assertThat(roleList).hasSize(2);
        assertThat(roleList.get(0).getId()).isEqualTo("f1abe44f-caaf-4352-8584-899fe89f458c");
        assertThat(roleList.get(0).getName()).isEqualTo("Administrator");

        assertThat(roleList.get(1).getId()).isEqualTo("e1ed52d3-19c0-46b7-82c1-1673dc5e8d64");
        assertThat(roleList.get(1).getName()).isEqualTo("User");
    }

    @Disabled
    @Test
    @DisplayName("Save Role - FAIL")
    @MongoDataFile(value = "roles.json", classType = Role.class, collectionName = "roles")
    public void whenSaveTwoRolesWithSameName_thenError() {
        assertThrows(RuntimeException.class, () -> {
            Role role = Role.builder().name("Administrator").version(1).build();
            Role role2 = Role.builder().name("Administrator").version(1).build();

            Role savedRole = roleRepository.save(role);
            Role savedRole2 = roleRepository.save(role2);
        });
    }

    @Test
    @DisplayName("Delete Role - SUCCESS")
    @MongoDataFile(value = "roles.json", classType = Role.class, collectionName = "roles")
    public void whenDeleteRoleFromMongo_ThenRoleNoLongerExists() {
        List<Role> originalList = roleRepository.findAll();

        roleRepository.delete(originalList.get(1)); //"User" role

        List<Role> newList = roleRepository.findAll();

        assertThat(originalList).hasSize(2);
        assertThat(newList).hasSize(1);
        assertThat(newList.get(0).getName()).isEqualTo("Administrator");
    }
}