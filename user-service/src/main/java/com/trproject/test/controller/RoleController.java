package com.trproject.test.controller;

import com.trproject.test.domain.dao.RoleDao;
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
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequestMapping("/roles")
@Api(description = "Операции над ролями в системе")
public class RoleController {

    @Autowired
    RoleDao roleDao;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ApiOperation(value = "Создание новой роли")
    public ResponseEntity<Role> create(@ApiParam(value = "Объект роли для создания") @RequestBody @NotNull @Valid Role role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleDao.save(role));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Получение роли по идентификатору")
    public ResponseEntity<Role> getById(@ApiParam(value = "Идентификатор роли") @PathVariable("id") @Min(1) long id) {
        Optional<Role> result = roleDao.findById(id);

        if (!result.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(result.get());

    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ApiOperation(value = "Получение всех ролей")
    public Iterable<Role> getAll() {
        return roleDao.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Удаление роли по идентификатору")
    public ResponseEntity delete(@ApiParam(value = "Идентификатор роли") @PathVariable("id") @Min(1) long id) {
        if (!roleDao.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        roleDao.deleteById(id);
        roleDao.flush();

        return ResponseEntity.ok().build();
    }
}
