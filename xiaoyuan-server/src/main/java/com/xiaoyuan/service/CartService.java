package com.xiaoyuan.service;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.entity.Cart;
import com.xiaoyuan.entity.Product;
import com.xiaoyuan.mapper.CartMapper;
import com.xiaoyuan.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 购物车服务
 */
@Service
public class CartService {

    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    public CartService(CartMapper cartMapper, ProductMapper productMapper) {
        this.cartMapper = cartMapper;
        this.productMapper = productMapper;
    }

    public List<Cart> list(Long userId) {
        return cartMapper.selectByUserId(userId);
    }

    /**
     * 加入购物车：已存在则累加数量
     */
    public Cart add(Long userId, Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BusinessException("数量必须大于0");
        }
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        Cart exist = cartMapper.selectByUserAndProduct(userId, productId);
        if (exist != null) {
            exist.setQuantity(exist.getQuantity() + quantity);
            cartMapper.update(exist);
            return exist;
        }
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setProductId(productId);
        cart.setQuantity(quantity);
        cartMapper.insert(cart);
        return cart;
    }

    public void updateQuantity(Long userId, Long cartId, Integer quantity) {
        Cart cart = getById(userId, cartId);
        if (quantity == null || quantity <= 0) {
            throw new BusinessException("数量必须大于0");
        }
        cart.setQuantity(quantity);
        cartMapper.update(cart);
    }

    public void delete(Long userId, Long cartId) {
        getById(userId, cartId);
        cartMapper.deleteById(cartId);
    }

    public void clear(Long userId) {
        cartMapper.clearByUserId(userId);
    }

    private Cart getById(Long userId, Long cartId) {
        Cart cart = cartMapper.selectByUserId(userId).stream()
                .filter(c -> c.getId().equals(cartId))
                .findFirst().orElse(null);
        if (cart == null) {
            throw new BusinessException("购物车条目不存在");
        }
        return cart;
    }
}
