package com.rhinoceros.mall.web.controller;

import com.rhinoceros.mall.core.pojo.CartProduct;
import com.rhinoceros.mall.core.pojo.User;
import com.rhinoceros.mall.core.vo.ProductVo;
import com.rhinoceros.mall.service.service.CartProductService;
import com.rhinoceros.mall.service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

@Controller
public class CartProductController {
    @Autowired
    private CartProductService cartProductService;
    @Autowired
    private ProductService productService;


    /**
     * 浏览购物车商品信息
     */
    @RequestMapping("/cart/list")
    public String cart(Model model, HttpSession session, HttpServletResponse response, HttpServletRequest request){
        User user = (User) session.getAttribute(LoginController.USERNAME);
        if (user == null) {
            return "redirect:/login?from=/cart/list";
        }
        List<CartProduct> cartProducts = cartProductService.findByUserId(user.getId());
        List<ProductVo> products = new LinkedList<ProductVo>();
        for (int i = 0; i < cartProducts.size(); i++) {
            Long tempID = cartProducts.get(i).getProductId();
            ProductVo product = productService.findProductVoById(tempID);
            products.add(product);
        }
        model.addAttribute("products", products);
        model.addAttribute("cartProducts", cartProducts);
        return "cart";
    }

    /**
     *修改购物车商品数量
     * @param cartProduct
     * @param session
     * @return
     */
    @RequestMapping("/cart/count")
    @ResponseBody
    public Integer countByCartProductId(CartProduct cartProduct ,HttpSession session) {
        User user = (User) session.getAttribute(LoginController.USERNAME);
        if (user == null) {
            return -1;
        }
        return cartProductService.updateSelectionById(cartProduct);
    }
    /**
     * 删除购物车商品信息
     *
     * @param cid
     */
    @ResponseBody
    @RequestMapping("/cart/delete")
    public String deleteCartProducts(@RequestParam("cid") Long cid) {
        cartProductService.deleteByid(cid);
        return "success";
    }

}
