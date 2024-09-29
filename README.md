README

Overview

This document outlines the use cases, assumptions, limitations, and potential improvements for the RESTful API designed to manage and apply different types of discount coupons for an e-commerce platform. The API supports cart-wise, product-wise, and BxGy coupons and is designed to be easily extensible for new types of coupons in the future.

Use Cases Covered

        Coupon Types

            Cart-wise Discount: Applies a discount to the entire cart if the total amount exceeds a certain threshold.
            Product-wise Discount: Applies a discount to specific products.
            Buy X Get Y (BxGy) Discount: Offers a discount based on buying a certain quantity of products to get other products for free or at a discount.

        API Endpoints

            POST /coupons: Create a new coupon.
            GET /coupons: Retrieve all coupons.
            GET /coupons/{id}: Retrieve a specific coupon by its ID.
            PUT /coupons/{id}: Update a specific coupon by its ID.
            DELETE /coupons/{id}: Delete a specific coupon by its ID.
            POST /applicable-coupons: Fetch all applicable coupons for a given cart and calculate the total discount that will be applied by each coupon.
            POST /apply-coupon/{id}: Apply a specific coupon to the cart and return the updated cart with discounted prices for each item.

Implemented Cases

    Create Coupon

        Cart-wise Discount: Creates a coupon that applies a discount to the entire cart if the cart value exceeds a certain threshold.
        Product-wise Discount: Creates a coupon that applies a discount to specific products.
        BxGy Discount: Creates a coupon that offers a discount based on buying a certain quantity of products to get other products for free or at a discount.
        Retrieve Coupons

    Get All Coupons: Retrieves a list of all available coupons.

    Get Coupon by ID: Retrieves a specific coupon by its ID.

    Update Coupon

        Cart-wise Discount: Updates the discount and threshold for a cart-wise coupon.
        Product-wise Discount: Updates the discount and product conditions for a product-wise coupon.
        BxGy Discount: Updates the buy and get conditions for a BxGy coupon.
        Delete Coupon

    Deletes a coupon and its associated conditions (if any).

    Get Applicable Coupons

        Cart-wise Coupons: Retrieves applicable cart-wise coupons based on the cart value.
        Product-wise Coupons: Retrieves applicable product-wise coupons based on the products in the cart.
        BxGy Coupons: Retrieves applicable BxGy coupons based on the products and quantities in the cart.

    Apply Coupon

        Cart-wise Discount: Applies a cart-wise discount to the cart.
        Product-wise Discount: Applies a product-wise discount to the cart.
        BxGy Discount: Applies a BxGy discount to the cart.

Unimplemented Cases:

    Complex Coupon Combinations:

        Description: Handling scenarios where multiple coupon types (e.g., cart-wise, product-wise, and BxGy) are applied simultaneously in a single transaction. This would involve defining rules for how the coupons interact and ensuring that they don’t conflict with each other.

        Reason for Not Implementing: Requires a more sophisticated algorithm to prioritize or sequence coupon application, which was beyond the scope of the current task.

    Dynamic Coupon Conditions:

        Description: Allowing dynamic conditions to be set for coupons, such as specific user behavior, time of day, or purchase history. This could include coupons with conditional triggers that are not predefined at the time of creation.

        Reason for Not Implementing: Setting up a flexible, dynamic condition system would require a more advanced rule engine or a flexible condition configuration that was not feasible within the time constraint.

    Coupon Expiry Notifications:

        Description: Implementing a notification system that alerts users when their coupons are close to expiring. This feature would involve tracking the expiration date and sending reminders to users.

        Reason for Not Implementing: Would require integrating with a notification system and setting up scheduled tasks to check expiration dates, which was not a priority for this iteration.

    User-Specific Coupons:

        Description: Creating coupons that are only available to specific users or groups of users. This would involve linking coupons to user profiles and restricting access based on user attributes.

        Reason for Not Implementing: Implementing user-specific logic would require a deeper integration with the user management system, which was outside the scope of the current task.

    Geolocation-Based Coupons:

        Description: Coupons that are valid only in specific geographical regions. For instance, certain discounts could only apply to users from certain cities, countries, or regions.

        Reason for Not Implementing: Requires integration with geolocation services or user address data, which was not prioritized for this task.

    Time-Limited Coupons:

        Description: Coupons that are valid only during specific hours of the day or on certain days. For example, a coupon might only work during the weekend or within a limited timeframe.

        Reason for Not Implementing: Adding time-based conditions and implementing a scheduler to monitor the activation window for coupons would require additional logic and time, which was out of scope.

    Coupon Stacking:

        Description: Allowing users to apply multiple coupons at once (e.g., a cart-wise coupon combined with a product-wise coupon). This would involve creating rules for how discounts are combined (e.g., whether they are applied sequentially or capped at a certain value).

        Reason for Not Implementing: Handling multiple coupons with stacking rules would require more complex discount calculation logic and conflict resolution, which was not feasible given the time constraints.

    Automatic Coupon Application:

        Description: Automatically applying the best coupon or combination of coupons to the cart without requiring the user to input a coupon code manually.

        Reason for Not Implementing: Implementing an auto-apply feature would need more sophisticated optimization algorithms to determine which coupon offers the maximum benefit to the user, which was beyond the current scope.

    Coupon Limits by User or Order:

        Description: Limiting the usage of coupons by certain criteria, such as a maximum number of uses per user or per order (e.g., a user can only use the coupon once, or a coupon can only be applied to orders above a certain value).

        Reason for Not Implementing: While simple usage limits were implemented, more granular control over these limits would require additional validation logic tied to user accounts or order histories.

    Referral-Based Coupons:

        Description: Implementing referral-based coupons where users get a discount for referring new users or if their referred users make a purchase.

        Reason for Not Implementing: Requires integration with a referral system, tracking users' referrals, and linking coupon distribution to referral activities, which was beyond the current scope.

    Tiered Discounts:

        Description: Coupons that offer a tiered discount, such as "10% off for orders above ₹500, 20% off for orders above ₹1000".

        Reason for Not Implementing: Would require implementing multiple discount thresholds and ensuring the correct tier applies based on the cart value, which adds complexity to the discount calculation logic.

    Recurring Coupons:

        Description: Coupons that can be used repeatedly over a period (e.g., "10% off every purchase for the next month").

        Reason for Not Implementing: Requires tracking coupon usage over time and implementing rules for when the coupon can be used again, which was beyond the current feature set.

    Bundled Product Coupons:

        Description: Coupons that apply only when specific products are bought together as a bundle (e.g., a discount when buying both Product A and Product B).

        Reason for Not Implementing: Requires complex logic to verify that specific combinations of products are present in the cart, which wasn’t feasible within the given timeframe.
