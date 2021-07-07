package com.ssm.mall.common;

public enum Result {
    RESULT_SUCCESS(200,"SUCCESS"),
    RESULT_ERROR(500,"ERROR"),
    USERNAME_IS_NOT_EXIST(103,"用户名不存在！"),
    PASSWORD_IS_WRONG(104,"密码错误"),
    EMAIL_ALREADY_EXIST(105,"EMAIL邮箱已存在"),
    USER_ALREADY_EXIST(106,"用户已经存在"),
    REGISTRY_ERROR(107,"注册失败"),
    CONFIG_READ_ERROR(108,"配置读取错误"),
    NEED_LOGIN(109,"用户尚未登录"),
    NO_PASSWORD_RESET_QUESTION(110,"对不起，您没有设置重置密码的预设问题"),
    PASSWORD_RESET_ANSWER_ERROR(111,"预设问题答案不符，无法重置密码"),
    NEED_TOKEN(112,"参数错误，需要传递token令牌"),
    TOKEN_EXPIRE(113,"token令牌已过期"),
    TOKEN_ERROR(114,"token令牌无效，请重新获取"),
    PASSWORD_RESET_ERROR(115,"密码重置失败"),
    MODIFY_PASSWORD_ERROR(116,"密码修改失败"),
    ORIGIN_PASSWORD_ERROR(117,"原始密码校验失败"),
    USER_NOT_FOUND(118,"用户未找到"),
    MODIFY_USER_ERROR(119,"修改用户失败"),
    ADMIN_LOGIN_ERROR(120,"您没有管理员权限，无权登录管理系统"),

    LOGIN_SUCCESS(201,"登录成功"),
    LOGOUT_SUCCESS(202,"注销成功"),
    REGISTRY_VALID_SUCCESS(203,"注册名称验证成功，用户名或邮箱可用"),
    REGISTRY_SUCCESS(204,"注册成功"),
    PASSWORD_RESET_SUCCESS(205,"密码重置成功"),
    MODIFY_PASSWORD_SUCCESS(205,"密码修改成功"),

    //商品目录模块-SUCCESS

    CATEGORY_ADD_SUCCESS(207,"商品目录添加成功"),
    CATEGORY_UPDATE_SUCCESS(208,"商品目录更新成功"),
    CATEGORY_CONFLICT_NAME_IN_SAME_PARENTID(300,"兄弟分类名冲突"),
    //商品目录模块-ERROR
    CATEGORY_ADD_ERROR(121,"目录添加失败"),
    ILLEAGLE_ARGUMENT(122,"非法参数"),
    CATEGORY_UPDATE_ERROR(123,"商品目录更新失败"),
    CATEGORY_NO_CHILDREN(124,"该商品目录没有子目录"),
    CATEGORY_NO_FOUND(125,"该类商品不存在或已下架"),
    //3-商品管理模块
    INSERT_PRODUCT_SUCCESS(301,"商品插入成功"),
    UPDATE_PRODUCT_SUCCESS(302,"商品修改成功"),
    UPDATE_STATUS_SUCCESS(303,"更新商品状态（上架或下架）成功"),
    PRODUCT_DETAIL_SUCCESS(304,"获取商品信息成功"),
    UPLOAD_IMAGE_SUCCESS(305,"上传商品图片成功"),
    NEED_ADMIN_LOGIN(361,"请登录管理员账号后，进行操作"),
    INSERT_PRODUCT_ERROR(362,"商品插入失败"),
    UPDATE_PRODUCT_ERROR(362,"商品修改失败"),
    UPDATE_STATUS_ERROR(363,"更新商品状态（上架或下架）失败"),
    PRODUCT_NOT_FOUND(364,"商品已下架或删除"),
    PRODUCT_UPLOAD_IMAGE_ERROR(365,"上传商品图片失败"),




    ORDER_NOT_FOUND(701,"该用户没有此订单，请审核订单编号是否正确"),
    ALIPAY_TRADE_FAILED(702,"支付宝预下单失败!!!"),
    ALIPAY_TRADE_STATE_UNKNOWN(703,"系统异常，预下单状态未知!!!"),
    ALIPAY_TRADE_NOT_SUPPLY(704,"不支持的交易状态，交易返回异常!!!"),
    ALIPAY_ILLEGAL_REQUEST_WARN(705,"非法的支付宝请求，如再次请求，将上报网警"),
    //4-支付和订单模块
    NO_CART_SELECTED(706,"购物车中没有产品被选中，无法生成订单"),
    PRODUCT_SALE_FAILED(707,"产品销售状态非在售"),
    STOCK_NOT_ENOUGH(708,"产品库存不足"),
    ORDER_CREATE_FAILED(709,"订单生成失败"),
    ORDER_ITEMS_BATCH_INSERT_FAILED(710,"订单详情批量插入错误"),
    ORDER_CREATE_SUCCESS(711,"订单生成成功"),
    HAVE_NO_ORDERS(712,"没有该订单"),
    ORDER_CANCEL_FAILED_ALEADY_PAY(713,"取消订单失败，不是未支付状态"),
    ORDER_NOT_PAY(714,"订单尚未付款"),




    REGISTRY_ILLEAGEL_ARGUMENT(121,"参数类型错误，只能选择用户名或EMAIL邮箱");




    private final int code;
    private final String msg;
    Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
