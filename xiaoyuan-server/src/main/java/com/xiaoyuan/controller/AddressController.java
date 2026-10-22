package com.xiaoyuan.controller;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.common.Result;
import com.xiaoyuan.entity.Address;
import com.xiaoyuan.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收货地址接口
 */
@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    private Long getUserId(HttpServletRequest request) {
        Object id = request.getAttribute("userId");
        if (id == null) {
            throw new BusinessException(401, "未登录");
        }
        return (Long) id;
    }

    /** 我的地址列表 */
    @GetMapping("/list")
    public Result<List<Address>> list(HttpServletRequest request) {
        return Result.success(addressService.list(getUserId(request)));
    }

    /** 新增地址 */
    @PostMapping
    public Result<Address> create(@RequestBody Address address, HttpServletRequest request) {
        return Result.success("新增成功", addressService.create(address, getUserId(request)));
    }

    /** 更新地址 */
    @PutMapping("/{id}")
    public Result<Address> update(@PathVariable Long id, @RequestBody Address address, HttpServletRequest request) {
        address.setId(id);
        return Result.success("更新成功", addressService.update(address, getUserId(request)));
    }

    /** 删除地址 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        addressService.delete(id, getUserId(request));
        return Result.success("删除成功", null);
    }
}
