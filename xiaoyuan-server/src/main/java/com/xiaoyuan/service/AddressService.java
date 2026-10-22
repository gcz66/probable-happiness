package com.xiaoyuan.service;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.entity.Address;
import com.xiaoyuan.mapper.AddressMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收货地址服务
 */
@Service
public class AddressService {

    private final AddressMapper addressMapper;

    public AddressService(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    public List<Address> list(Long userId) {
        return addressMapper.selectByUserId(userId);
    }

    public Address getById(Long id, Long userId) {
        Address address = addressMapper.selectById(id);
        if (address == null) {
            throw new BusinessException("地址不存在");
        }
        if (!address.getUserId().equals(userId)) {
            throw new BusinessException("无权操作该地址");
        }
        return address;
    }

    @Transactional
    public Address create(Address address, Long userId) {
        address.setUserId(userId);
        // 首个地址自动设为默认，否则默认非默认
        if (addressMapper.selectByUserId(userId).isEmpty()) {
            address.setIsDefault(1);
        } else if (address.getIsDefault() == null) {
            address.setIsDefault(0);
        }
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            addressMapper.clearDefault(userId);
        }
        addressMapper.insert(address);
        return address;
    }

    @Transactional
    public Address update(Address address, Long userId) {
        getById(address.getId(), userId);
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            addressMapper.clearDefault(userId);
        }
        addressMapper.update(address);
        return getById(address.getId(), userId);
    }

    public void delete(Long id, Long userId) {
        getById(id, userId);
        addressMapper.deleteById(id);
    }
}
