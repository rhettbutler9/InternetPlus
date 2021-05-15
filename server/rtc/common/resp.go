package common

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/pkg/errors"
)

func NewError(ctx *gin.Context, err error, msg string) {
	er := R{
		Code:   http.StatusInternalServerError,
		ErrMsg: errors.WithMessage(err, msg).Error(),
	}
	ctx.JSON(http.StatusBadRequest, er)
}

type HTTPError struct {
	Code    int    `json:"code" example:"400"`
	Message string `json:"message" example:"status bad request"`
}

// data:{
//         token_valid:, // true valid
//         code: ,// 200 success
//         err_message: //
//         data:{ // translate data
//         }

type R struct {
	TokenValid bool        `json:"token_valid"`
	Code       int         `json:"code"`
	ErrMsg     string      `json:"err_message"`
	PayLoad    interface{} `json:"data"`
}

func NewR() *R {
	return &R{
		TokenValid: true,
		Code:       200,
		ErrMsg:     "",
		PayLoad:    nil,
	}
}

//NewBadR use for bad request
func NewBadR(err error) *R {
	return &R{
		TokenValid: false,
		Code:       http.StatusBadRequest,
		ErrMsg:     err.Error(),
		PayLoad:    nil,
	}
}


func NewErrorR(err error) *R{

	return &R{
		TokenValid: true,
		Code:       http.StatusBadRequest,
		ErrMsg:     err.Error(),
		PayLoad:    nil,
	}
}

type Response struct {
	Code int         `json:"code"`
	Data interface{} `json:"data"`
	Msg  string      `json:"msg"`
}


func Result(code int, data interface{}, msg string, c *gin.Context) {
	// 开始时间
	c.JSON(code , R{
		true,
		code,
		msg,
		data,
	})
}

func Ok(c *gin.Context) {
	Result(200, map[string]interface{}{}, "操作成功", c)
}

func OkWithMessage(message string, c *gin.Context) {
	Result(200, map[string]interface{}{}, message, c)
}

func OkWithData(data interface{}, c *gin.Context) {
	Result(200, data, "操作成功", c)
}

func OkWithDetailed(code int, data interface{}, message string, c *gin.Context) {
	Result(code , data, message, c)
}

func Fail(c *gin.Context) {
	Result(http.StatusBadRequest, map[string]interface{}{}, "操作失败", c)
}

func FailWithMessage(message string, c *gin.Context) {
	Result(http.StatusBadRequest, map[string]interface{}{}, message, c)
}

func FailWithDetailed(code int, data interface{}, message string, c *gin.Context) {
	Result(code, data, message, c)
}



