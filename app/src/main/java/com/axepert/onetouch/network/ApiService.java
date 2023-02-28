package com.axepert.onetouch.network;

import com.axepert.onetouch.requests.AddAddressRequest;
import com.axepert.onetouch.requests.AddBlogRequest;
import com.axepert.onetouch.requests.AddReviewRequest;
import com.axepert.onetouch.requests.AddressListRequest;
import com.axepert.onetouch.requests.BookServiceRequest;
import com.axepert.onetouch.requests.ChangePasswordRequest;
import com.axepert.onetouch.requests.DashboradRequest;
import com.axepert.onetouch.requests.EditDealerProfileRequest;
import com.axepert.onetouch.requests.EditProfileRequest;
import com.axepert.onetouch.requests.InvoiceRequest;
import com.axepert.onetouch.requests.LoginRequest;
import com.axepert.onetouch.requests.MyServiceCategoryRequest;
import com.axepert.onetouch.requests.OrderCodRequest;
import com.axepert.onetouch.requests.OrderOnlineRequest;
import com.axepert.onetouch.requests.ProductByCarRequest;
import com.axepert.onetouch.requests.ProductDetailsRequest;
import com.axepert.onetouch.requests.ProductListRequest;
import com.axepert.onetouch.requests.RegisterRequest;
import com.axepert.onetouch.requests.RelatedProductRequest;
import com.axepert.onetouch.requests.RelatedServiceProvidersRequest;
import com.axepert.onetouch.requests.SearchProductRequest;
import com.axepert.onetouch.requests.SubCategoryRequest;
import com.axepert.onetouch.responses.AddAddressResponse;
import com.axepert.onetouch.responses.AddReviewResponse;
import com.axepert.onetouch.responses.AddressListResponse;
import com.axepert.onetouch.responses.BookServiceResponse;
import com.axepert.onetouch.responses.ChangePasswordResponse;
import com.axepert.onetouch.responses.DashboardResponse;
import com.axepert.onetouch.responses.ECommHomePageResponse;
import com.axepert.onetouch.responses.EditProfileResponse;
import com.axepert.onetouch.responses.HomeScreenResponse;
import com.axepert.onetouch.responses.InvoiceResponse;
import com.axepert.onetouch.responses.LoginResponse;
import com.axepert.onetouch.responses.MyServiceCategoryResponse;
import com.axepert.onetouch.responses.MyServiceSubCategoryResponse;
import com.axepert.onetouch.responses.OrdersResponse;
import com.axepert.onetouch.responses.PlaceOrderResponse;
import com.axepert.onetouch.responses.ProductByCatResponse;
import com.axepert.onetouch.responses.ProductDetailsResponse;
import com.axepert.onetouch.responses.ProductListResponse;
import com.axepert.onetouch.responses.RegistrationResponse;
import com.axepert.onetouch.responses.RelatedProductResponse;
import com.axepert.onetouch.responses.RelatedServiceProvidersResponse;
import com.axepert.onetouch.responses.ReviewsResponse;
import com.axepert.onetouch.responses.SearchProductResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/Register/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("onetouch/api/user/login")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

    @GET("api/HomeScreen/homescreen_details")
    Call<HomeScreenResponse> getHomeData();

    @POST("api/Register/registration")
    Call<RegistrationResponse> register(@Body RegisterRequest registerRequest);

    @POST("onetouch/api/user/registration")
    Call<RegistrationResponse> userRegister(@Body RegisterRequest registerRequest);

    @POST("api/HomeScreen/get_provider_with_serv_id")
    Call<RelatedServiceProvidersResponse> getServiceProviders(@Body RelatedServiceProvidersRequest relatedServiceProvidersRequest);

    @POST("api/HomeScreen/bookservices")
    Call<BookServiceResponse> bookService(@Body BookServiceRequest bookServiceRequest);

    @POST("api/HomeScreen/add_review")
    Call<AddReviewResponse> addReview(@Body AddReviewRequest addReviewRequest);

    @GET("onetouch/api/AxesellEcommerce/homescreen_details")
    Call<ECommHomePageResponse> getEcommerceHomePage();

    @POST("onetouch/api/Productdetails/details")
    Call<ProductDetailsResponse> getProductDetails(@Body ProductDetailsRequest productDetailsRequest);

    @POST("onetouch/api/Productlist/searchproduct")
    Call<SearchProductResponse> getProductBySearch(@Body SearchProductRequest searchProductRequest);

    @POST("onetouch/api/productbycat/productbycat")
    Call<ProductByCatResponse> productByCategory(@Body ProductByCarRequest productByCarRequest);

    @POST("onetouch/api/user/getuseraddress")
    Call<AddressListResponse> getAddressList(@Body AddressListRequest addressListRequest);

    @POST("onetouch/api/user/useraddaddress")
    Call<AddAddressResponse> addAddress(@Body AddAddressRequest addAddressRequest);

    @POST("onetouch/api/user/edituseraddress")
    Call<AddAddressResponse> editAddress(@Body AddAddressRequest addAddressRequest);

    @POST("onetouch/api/orderdetails/index")
    Call<OrdersResponse> fetchOrders(@Body AddressListRequest addressListRequest);

    @POST("onetouch/api/Relatedproducts/productbyslug")
    Call<RelatedProductResponse> relatedProduct(@Body RelatedProductRequest relatedProductRequest);

    @POST("onetouch/api/userlogin/changepassword2")
    Call<ChangePasswordResponse> changePassword(@Body ChangePasswordRequest changePasswordRequest);

    @POST("onetouch/api/user/userdataupdate")
    Call<EditProfileResponse> editUserProfile(@Body EditProfileRequest editProfileRequest);

    @POST("onetouch/api/Userorder/userordercod")
    Call<PlaceOrderResponse> orderCod(@Body OrderCodRequest orderCodRequest);

    @POST("onetouch/api/Userorder/userorderonline")
    Call<PlaceOrderResponse> orderOnline(@Body OrderOnlineRequest onlineRequest);

    @POST("onetouch/api/productlist/productlist")
    Call<ProductListResponse> productList(@Body ProductListRequest productListRequest);

    @POST("onetouch/api/Userorder/userorderdetails")
    Call<InvoiceResponse> invoice(@Body InvoiceRequest invoiceRequest);

    @POST("api/Register/add_blog2")
    Call<PlaceOrderResponse> addBlog(@Body AddBlogRequest addBlogRequest);

    @POST("api/User_profile/seller_dashboard")
    Call<DashboardResponse> dashboard(@Body DashboradRequest dashboradRequest);

    @POST("api/HomeScreen/get_cat_by_seller")
    Call<MyServiceCategoryResponse> myServiceCategory(@Body MyServiceCategoryRequest myServiceCategoryRequest);

    @POST("api/HomeScreen/get_sub_cat_by_seller")
    Call<MyServiceSubCategoryResponse> myServiceSubCategory(@Body MyServiceCategoryRequest myServiceCategoryRequest);

    @POST("api/HomeScreen/get_reviews")
    Call<ReviewsResponse> myReviews(@Body MyServiceCategoryRequest myServiceCategoryRequest);

    @POST("api/user_profile/subcategourybyid")
    Call<MyServiceCategoryResponse> subCategory(@Body SubCategoryRequest subCategoryRequest);

    @POST("api/Register/user_details_update")
    Call<PlaceOrderResponse> editDealerProfile(@Body EditDealerProfileRequest editDealerProfileRequest);

}
