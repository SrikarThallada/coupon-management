package com.monkcommerce.couponmanagement.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.monkcommerce.couponmanagement.dto.ApplicableCouponsDTO;
import com.monkcommerce.couponmanagement.dto.ApplicableDiscountDTO;
import com.monkcommerce.couponmanagement.dto.BxgyDiscountDTO;
import com.monkcommerce.couponmanagement.dto.CartDTO;
import com.monkcommerce.couponmanagement.dto.CartItemDTO;
import com.monkcommerce.couponmanagement.dto.CartWiseDiscountDTO;
import com.monkcommerce.couponmanagement.dto.CouponDTO;
import com.monkcommerce.couponmanagement.dto.ProductWiseDiscountDTO;
import com.monkcommerce.couponmanagement.dto.UpdateCartDTO;
import com.monkcommerce.couponmanagement.entity.BxGyCondition;
import com.monkcommerce.couponmanagement.entity.Coupon;
import com.monkcommerce.couponmanagement.entity.ProductCondition;
import com.monkcommerce.couponmanagement.enums.CouponType;
import com.monkcommerce.couponmanagement.exception.CouponException;
import com.monkcommerce.couponmanagement.repository.BxGyConditionRepo;
import com.monkcommerce.couponmanagement.repository.CouponRepo;
import com.monkcommerce.couponmanagement.repository.ProductConditionRepo;

@Service
public class CouponServiceImpl implements CouponService {

    private CouponRepo couponRepo;
    private ProductConditionRepo productConditionRepo;
    private BxGyConditionRepo bxGyConditionRepo;

    public CouponServiceImpl(CouponRepo couponRepo, ProductConditionRepo productConditionRepo,
            BxGyConditionRepo bxGyConditionRepo) {
        this.couponRepo = couponRepo;
        this.productConditionRepo = productConditionRepo;
        this.bxGyConditionRepo = bxGyConditionRepo;
    }

    @Override
    public CouponDTO createCoupon(CouponDTO couponDTO) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String type = couponDTO.getType();
        Coupon coupon = new Coupon();
        if (couponDTO.getExpirationDate().isBefore(java.time.LocalDate.now())) {
            throw new CouponException("Expiration date must be in the future");
        }
        coupon.setExpirationDate(couponDTO.getExpirationDate());

        Coupon addedCoupon;
        switch (type) {
            case "cart-wise":
                addedCoupon = handleCartWiseDiscount(couponDTO, coupon, objectMapper);
                break;
            case "product-wise":
                addedCoupon = handleProductWiseDiscount(couponDTO, coupon, objectMapper);
                break;
            case "bxgy":
                addedCoupon = handleBxgyDiscount(couponDTO, coupon, objectMapper);
                break;
            default:
                throw new CouponException("Invalid coupon type");
        }

        couponDTO.setId(addedCoupon.getId());
        return couponDTO;
    }

    private Coupon handleCartWiseDiscount(CouponDTO couponDTO, Coupon coupon, ObjectMapper objectMapper)
            throws Exception {
        CartWiseDiscountDTO cartWiseDiscount = objectMapper.treeToValue(couponDTO.getDetails(),
                CartWiseDiscountDTO.class);
        coupon.setType(CouponType.CART_WISE);
        coupon.setDiscount(cartWiseDiscount.getDiscount());
        coupon.setThreshold(cartWiseDiscount.getThreshold());
        return couponRepo.save(coupon);
    }

    private Coupon handleProductWiseDiscount(CouponDTO couponDTO, Coupon coupon, ObjectMapper objectMapper)
            throws Exception {
        ProductWiseDiscountDTO productWiseDiscount = objectMapper.treeToValue(couponDTO.getDetails(),
                ProductWiseDiscountDTO.class);
        coupon.setType(CouponType.PRODUCT_WISE);
        coupon.setDiscount(productWiseDiscount.getDiscount());
        Coupon addedCoupon = couponRepo.save(coupon);

        ProductCondition productCondition = new ProductCondition();
        productCondition.setCoupon(addedCoupon);
        productCondition.setProductId(productWiseDiscount.getProductId());
        productConditionRepo.save(productCondition);

        return addedCoupon;
    }

    private Coupon handleBxgyDiscount(CouponDTO couponDTO, Coupon coupon, ObjectMapper objectMapper) throws Exception {
        BxgyDiscountDTO bxgyDiscount = objectMapper.treeToValue(couponDTO.getDetails(), BxgyDiscountDTO.class);
        coupon.setType(CouponType.BXGY);
        Coupon addedCoupon = couponRepo.save(coupon);

        BxGyCondition bxGyCondition = new BxGyCondition();
        bxGyCondition.setCoupon(coupon);
        bxGyCondition.setBuyProducts(objectMapper.readTree(bxgyDiscount.getBuyProducts().toString()));
        bxGyCondition.setGetProducts(objectMapper.readTree(bxgyDiscount.getBuyProducts().toString()));
        bxGyCondition.setRepetitionLimit(bxgyDiscount.getRepetitionLimit());
        bxGyConditionRepo.save(bxGyCondition);

        return addedCoupon;
    }

    @Override
    public List<CouponDTO> getAllCoupons() {
        List<Coupon> coupons = couponRepo.findAll();
        return coupons.stream()
                .map(this::convertCouponToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CouponDTO getCouponById(Long id) {
        Optional<Coupon> optionalCoupon = couponRepo.findById(id);
        if (optionalCoupon.isEmpty()) {
            throw new CouponException("Coupon not found");
        }
        return convertCouponToDTO(optionalCoupon.get());
    }

    private CouponDTO convertCouponToDTO(Coupon coupon) {
        ObjectMapper objectMapper = new ObjectMapper();
        CouponType type = coupon.getType();
        CouponDTO couponDTO = new CouponDTO();
        couponDTO.setId(coupon.getId());
        couponDTO.setExpirationDate(coupon.getExpirationDate());

        switch (type) {
            case CART_WISE:
                CartWiseDiscountDTO cartWiseDiscount = new CartWiseDiscountDTO(coupon.getThreshold(),
                        coupon.getDiscount());
                couponDTO.setType("cart-wise");
                couponDTO.setDetails(objectMapper.valueToTree(cartWiseDiscount));
                break;
            case PRODUCT_WISE:
                Optional<ProductCondition> productCondition = productConditionRepo.findByCouponId(coupon.getId());
                ProductWiseDiscountDTO productWiseDiscount = new ProductWiseDiscountDTO(
                        productCondition.get().getProductId(), coupon.getDiscount());
                couponDTO.setType("product-wise");
                couponDTO.setDetails(objectMapper.valueToTree(productWiseDiscount));
                break;
            case BXGY:
                BxGyCondition bxGyCondition = bxGyConditionRepo.findByCouponId(coupon.getId()).get();
                BxgyDiscountDTO bxgyDiscount = new BxgyDiscountDTO();
                bxgyDiscount.setBuyProducts(
                        convertArrayNodeToList(objectMapper.valueToTree(bxGyCondition.getBuyProducts())));
                bxgyDiscount.setGetProducts(
                        convertArrayNodeToList(objectMapper.valueToTree(bxGyCondition.getGetProducts())));
                bxgyDiscount.setRepetitionLimit(bxGyCondition.getRepetitionLimit());
                couponDTO.setType("bxgy");
                couponDTO.setDetails(objectMapper.valueToTree(bxgyDiscount));
                break;
            default:
                throw new CouponException("Invalid coupon type");
        }
        return couponDTO;
    }

    private List<JsonNode> convertArrayNodeToList(JsonNode jsonNode) {
        List<JsonNode> list = new ArrayList<>();
        if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            arrayNode.forEach(list::add);
        }
        return list;
    }

    @Override
    public CouponDTO updateCoupon(Long id, CouponDTO couponDTO) throws Exception {
        Optional<Coupon> optionalCoupon = couponRepo.findById(id);
        ObjectMapper objectMapper = new ObjectMapper();

        if (optionalCoupon.isEmpty()) {
            throw new CouponException("Coupon not found");
        }

        Coupon existingCoupon = optionalCoupon.get();
        existingCoupon.setExpirationDate(couponDTO.getExpirationDate());

        switch (existingCoupon.getType()) {
            case CART_WISE:
                updateCartWiseDiscount(existingCoupon, couponDTO, objectMapper);
                break;
            case PRODUCT_WISE:
                updateProductWiseDiscount(existingCoupon, couponDTO, objectMapper);
                break;
            case BXGY:
                updateBxgyDiscount(existingCoupon, couponDTO, objectMapper);
                break;
            default:
                throw new CouponException("Invalid coupon type");
        }

        couponRepo.save(existingCoupon);
        return couponDTO;
    }

    private void updateCartWiseDiscount(Coupon existingCoupon, CouponDTO couponDTO, ObjectMapper objectMapper)
            throws Exception {
        CartWiseDiscountDTO cartWiseDiscount = objectMapper.treeToValue(couponDTO.getDetails(),
                CartWiseDiscountDTO.class);
        existingCoupon.setDiscount(cartWiseDiscount.getDiscount());
        existingCoupon.setThreshold(cartWiseDiscount.getThreshold());
    }

    private void updateProductWiseDiscount(Coupon existingCoupon, CouponDTO couponDTO, ObjectMapper objectMapper)
            throws Exception {
        ProductWiseDiscountDTO productWiseDiscount = objectMapper.treeToValue(couponDTO.getDetails(),
                ProductWiseDiscountDTO.class);
        ProductCondition productCondition = productConditionRepo.findByCouponId(existingCoupon.getId()).get();
        productCondition.setProductId(productWiseDiscount.getProductId());
        productConditionRepo.save(productCondition);
        existingCoupon.setDiscount(productWiseDiscount.getDiscount());
    }

    private void updateBxgyDiscount(Coupon existingCoupon, CouponDTO couponDTO, ObjectMapper objectMapper)
            throws Exception {
        BxgyDiscountDTO bxgyDiscount = objectMapper.treeToValue(couponDTO.getDetails(), BxgyDiscountDTO.class);
        BxGyCondition bxGyCondition = bxGyConditionRepo.findByCouponId(existingCoupon.getId()).get();
        bxGyCondition.setBuyProducts(objectMapper.readTree(bxgyDiscount.getBuyProducts().toString()));
        bxGyCondition.setGetProducts(objectMapper.readTree(bxgyDiscount.getBuyProducts().toString()));
        bxGyCondition.setRepetitionLimit(bxgyDiscount.getRepetitionLimit());
        bxGyConditionRepo.save(bxGyCondition);
    }

    @Override
    public void deleteCoupon(Long id) {
        Optional<Coupon> optionalCoupon = couponRepo.findById(id);

        switch (optionalCoupon.get().getType()) {
            case PRODUCT_WISE:
                Optional<ProductCondition> productCondition = productConditionRepo.findByCouponId(id);
                productCondition.ifPresent(productConditionRepo::delete);
                break;
            case BXGY:
                Optional<BxGyCondition> bxGyCondition = bxGyConditionRepo.findByCouponId(id);
                bxGyCondition.ifPresent(bxGyConditionRepo::delete);
                break;
            default:
                break;
        }

        if (optionalCoupon.isEmpty()) {
            throw new CouponException("Coupon not found");
        }

        Coupon coupon = optionalCoupon.get();
        couponRepo.delete(coupon);
    }

    @Override
    public ApplicableCouponsDTO getApplicableCoupons(CartDTO cart) {
        ApplicableCouponsDTO applicableCouponsDTO = new ApplicableCouponsDTO();

        List<ApplicableDiscountDTO> applicableDiscountDTOCart = getApplicableCouponCartWise(cart);
        List<ApplicableDiscountDTO> applicableDiscountDTOProduct = getApplicableCouponsProductWise(cart);
        List<ApplicableDiscountDTO> applicableDiscountDTOBxGy = getApplicableCouponsBxGy(cart);

        List<ApplicableDiscountDTO> applicableDiscountDTOs = new ArrayList<>();
        applicableDiscountDTOs.addAll(applicableDiscountDTOCart);
        applicableDiscountDTOs.addAll(applicableDiscountDTOProduct);
        applicableDiscountDTOs.addAll(applicableDiscountDTOBxGy);

        applicableCouponsDTO.setApplicableCoupons(applicableDiscountDTOs);
        return applicableCouponsDTO;
    }

    private List<ApplicableDiscountDTO> getApplicableCouponCartWise(CartDTO cart) {
        Double price = cart.getItems().stream().mapToDouble(item -> item.getQuantity() * item.getPrice()).sum();
        List<Coupon> coupons = couponRepo.findByThresholdLessThanAndType(price, CouponType.CART_WISE);
        return coupons.stream().map(coupon -> {
            ApplicableDiscountDTO applicableDiscount = new ApplicableDiscountDTO();
            applicableDiscount.setDiscount(price * (coupon.getDiscount() / 100));
            applicableDiscount.setCouponId(coupon.getId());
            applicableDiscount.setType(coupon.getType().toString());
            return applicableDiscount;
        }).collect(Collectors.toList());
    }

    private List<ApplicableDiscountDTO> getApplicableCouponsProductWise(CartDTO cart) {
        return cart.getItems().stream().map(item -> {
            List<ProductCondition> productConditions = productConditionRepo.findByProductId(item.getProductId());
            return productConditions.stream().map(productCondition -> {
                Coupon coupon = productCondition.getCoupon();
                ApplicableDiscountDTO applicableDiscount = new ApplicableDiscountDTO();
                applicableDiscount.setDiscount((item.getPrice() * item.getQuantity()) * (coupon.getDiscount() / 100));
                applicableDiscount.setCouponId(coupon.getId());
                applicableDiscount.setType(coupon.getType().toString());
                return applicableDiscount;
            }).collect(Collectors.toList());
        }).flatMap(List::stream).collect(Collectors.toList());
    }

    public List<ApplicableDiscountDTO> getApplicableCouponsBxGy(CartDTO cart) {
        List<BxGyCondition> conditions = bxGyConditionRepo.findAll();
        List<ApplicableDiscountDTO> applicableCoupons = new ArrayList<>();

        for (BxGyCondition condition : conditions) {
            Map<Long, Integer> buyProductMap = getProductMap(condition.getBuyProducts());
            Map<Long, Integer> getProductMap = getProductMap(condition.getGetProducts());

            int buyCount = 0;
            Map<Long, Integer> cartProductMap = new HashMap<>();

            for (CartItemDTO item : cart.getItems()) {
                cartProductMap.put(item.getProductId(), item.getQuantity());
                if (buyProductMap.containsKey(item.getProductId())) {
                    buyCount += item.getQuantity();
                }
            }

            int requiredBuyCount = buyProductMap.values().stream().mapToInt(Integer::intValue).sum();
            int repetitionLimit = condition.getRepetitionLimit();
            int applicableTimes = Math.min(buyCount / requiredBuyCount, repetitionLimit);

            if (applicableTimes > 0 && checkBuyConditions(cartProductMap, buyProductMap, applicableTimes)) {
                double discount = calculateDiscount(cart, getProductMap, applicableTimes);
                ApplicableDiscountDTO couponDTO = new ApplicableDiscountDTO();
                couponDTO.setCouponId(condition.getCoupon().getId());
                couponDTO.setType("bxgy");
                couponDTO.setDiscount(discount);
                applicableCoupons.add(couponDTO);
            }
        }

        return applicableCoupons;
    }

    private Map<Long, Integer> getProductMap(JsonNode products) {
        Map<Long, Integer> productMap = new HashMap<>();
        for (JsonNode product : products) {
            productMap.put(product.get("product_id").asLong(), product.get("quantity").asInt());
        }
        return productMap;
    }

    private boolean checkBuyConditions(Map<Long, Integer> cartProductMap, Map<Long, Integer> buyProductMap,
            int applicableTimes) {
        for (Map.Entry<Long, Integer> entry : buyProductMap.entrySet()) {
            Long productId = entry.getKey();
            int requiredQuantity = entry.getValue() * applicableTimes;
            if (!cartProductMap.containsKey(productId) || cartProductMap.get(productId) < requiredQuantity) {
                return false;
            }
        }
        return true;
    }

    private double calculateDiscount(CartDTO cart, Map<Long, Integer> getProductMap, int applicableTimes) {
        double discount = 0;
        for (CartItemDTO item : cart.getItems()) {
            if (getProductMap.containsKey(item.getProductId())) {
                int freeQuantity = getProductMap.get(item.getProductId()) * applicableTimes;
                discount += freeQuantity * item.getPrice();
            }
        }
        return discount;
    }

    @Override
    public UpdateCartDTO applyCoupon(Long couponId, CartDTO cart) {
        Optional<Coupon> optionalCoupon = couponRepo.findById(couponId);

        if (optionalCoupon.isEmpty()) {
            throw new CouponException("Coupon not found");
        }

        Coupon coupon = optionalCoupon.get();

        if (coupon.getExpirationDate().isBefore(java.time.LocalDate.now())) {
            throw new CouponException("Coupon has expired");
        }

        switch (coupon.getType()) {
            case CART_WISE:
                return applyCartWiseDiscount(coupon, cart);
            case PRODUCT_WISE:
                return applyProductWiseDiscount(coupon, cart);
            case BXGY:
                return applyBxgyDiscount(coupon, cart);
            default:
                throw new CouponException("Invalid coupon type");
        }
    }

    private UpdateCartDTO applyCartWiseDiscount(Coupon coupon, CartDTO cart) {
        Double price = cart.getItems().stream().mapToDouble(item -> item.getQuantity() * item.getPrice()).sum();

        UpdateCartDTO updateCartDTO = new UpdateCartDTO();
        updateCartDTO.setItems(cart.getItems());
        if (price < coupon.getThreshold()) {
            throw new CouponException("Cart value is less than threshold");
        }

        double discount = price * (coupon.getDiscount() / 100);
        double finalPrice = price - discount;
        updateCartDTO.setTotalPrice(price);
        updateCartDTO.setTotalDiscount(discount);
        updateCartDTO.setFinalPrice(finalPrice);
        return updateCartDTO;
    }

    private UpdateCartDTO applyProductWiseDiscount(Coupon coupon, CartDTO cart) {
        UpdateCartDTO updateCartDTO = new UpdateCartDTO();
        updateCartDTO.setItems(cart.getItems());
        double totalDiscount = 0;
        for (CartItemDTO item : cart.getItems()) {
            Optional<ProductCondition> optionalProductCond = productConditionRepo
                    .findByProductIdAndCouponId(item.getProductId(), coupon.getId());
            if (optionalProductCond.isPresent()) {
                double discount = (item.getPrice() * item.getQuantity()) * (coupon.getDiscount() / 100);
                totalDiscount += discount;
            }
        }

        double price = cart.getItems().stream().mapToDouble(item -> item.getQuantity() * item.getPrice()).sum();
        double finalPrice = price - totalDiscount;

        updateCartDTO.setTotalPrice(price);
        updateCartDTO.setTotalDiscount(totalDiscount);
        updateCartDTO.setFinalPrice(finalPrice);
        return updateCartDTO;
    }

    private UpdateCartDTO applyBxgyDiscount(Coupon coupon, CartDTO cart) {
        UpdateCartDTO updateCartDTO = new UpdateCartDTO();
        updateCartDTO.setItems(cart.getItems());
        double totalDiscount = 0;

        Optional<BxGyCondition> optionalBxGyCondition = bxGyConditionRepo.findByCouponId(coupon.getId());
        BxGyCondition condition = optionalBxGyCondition.get();

        Map<Long, Integer> buyProductMap = getProductMap(condition.getBuyProducts());
        Map<Long, Integer> getProductMap = getProductMap(condition.getGetProducts());

        int buyCount = 0;
        Map<Long, Integer> cartProductMap = new HashMap<>();

        for (CartItemDTO item : cart.getItems()) {
            cartProductMap.put(item.getProductId(), item.getQuantity());
            if (buyProductMap.containsKey(item.getProductId())) {
                buyCount += item.getQuantity();
            }
        }

        int requiredBuyCount = buyProductMap.values().stream().mapToInt(Integer::intValue).sum();
        int repetitionLimit = condition.getRepetitionLimit();
        int applicableTimes = Math.min(buyCount / requiredBuyCount, repetitionLimit);

        if (applicableTimes > 0 && checkBuyConditions(cartProductMap, buyProductMap, applicableTimes)) {
            double discount = calculateDiscount(cart, getProductMap, applicableTimes);
            totalDiscount += discount;
        }

        double price = cart.getItems().stream().mapToDouble(item -> item.getQuantity() * item.getPrice()).sum();
        double finalPrice = price - totalDiscount;

        updateCartDTO.setTotalPrice(price);
        updateCartDTO.setTotalDiscount(totalDiscount);
        updateCartDTO.setFinalPrice(finalPrice);
        return updateCartDTO;
    }

}
