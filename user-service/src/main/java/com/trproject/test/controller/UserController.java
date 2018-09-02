package com.trproject.test.controller;

import com.trproject.test.domain.dao.RoleDao;
import com.trproject.test.domain.dao.UserDao;
import com.trproject.test.domain.model.Role;
import com.trproject.test.domain.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Api(description = "Операции над пользователями в системе")
public class UserController {

    @Autowired
    RoleDao roleDao;

    @Autowired
    UserDao userDao;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ApiOperation(value = "Создание нового пользователя")
    public ResponseEntity<User> create(@ApiParam(value = "Объект пользователя для создания") @RequestBody @NotNull @Valid User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userDao.saveAndFlush(user));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Получение пользователя по идентификатору")
    public ResponseEntity<User> getById(@ApiParam(value = "Идентификатор пользователя") @PathVariable("id") @Min(1) long id) {
        Optional<User> result = userDao.findById(id);

        if (!result.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(result.get());

    }

    /**
     * ВАЖНО!!! Такой кривой подход связан с автоматическим формированием документации.
     * Swagger не поддерживает несколько запросов с оним путем и разными параметрами.
     * Пришлось положить оба параметра в один обработчик. (До этого использовался параметр @RequestMapping.params для разграничения обработчиков запросов.)
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "Получение пользователей по имени или роли", response = User.class)
    public ResponseEntity<?> getByName(@ApiParam(value = "Имя пользователя") @RequestParam(value = "user-name", required = false) @NotEmpty String userName,
                                          @ApiParam(value = "Имя роли") @RequestParam(value = "role-name", required = false) @NotEmpty String roleName) {
        if (userName != null) {
            return findByUserName(userName);
        }

        if (roleName != null) {
            return findByRoleName(roleName);
        }

        return ResponseEntity.badRequest().body("Не определено ни одного параметра для поиска");
    }

    /**
     * Получение пользователя по имени.
     *
     * @param userName Имя пользователя.
     * @return Пользователь с заданным именем.
     */
    private ResponseEntity<?> findByUserName(String userName) {
        Optional<User> result = userDao.findByName(userName);

        if (!result.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(result.get());
    }

    /**
     * Получение пользователей по роли.
     *
     * @param roleName Имя роли.
     * @return Список пользователей роли.
     */
    private ResponseEntity<List<User>> findByRoleName(String roleName) {
        Optional<Role> role = roleDao.findByName(roleName);

        if (!role.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(role.get().getUsers());
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ApiOperation(value = "Получение всех пользователей")
    public Iterable<User> getAll() {
        return userDao.findAll();
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Удаление пользователя по идентификатору")
    public ResponseEntity delete(@ApiParam(value = "Идентификатор пользователя") @PathVariable("id") @Min(1) long id) {
        if (!userDao.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        userDao.deleteById(id);
        userDao.flush();

        return ResponseEntity.ok().build();
    }
}
