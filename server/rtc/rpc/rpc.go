package rpc

import (
	"github.com/gin-gonic/gin"
	"github.com/resyon/InternetPlus/server/rtc/common"
	"gopkg.in/h2non/gentleman.v2"
	"strconv"
)

var (
	_client = gentleman.New().BaseURL("http://127.0.0.1:9000/api/v1")
)

func init() {
	req := _client.Request().AddPath("/login")
	resp, err := req.JSON(gin.H{

	}).Do()

	if err != nil ||  resp.StatusCode != 200 || !resp.Ok {
		common.Log.Panicf("Can not register to main server : %s", err)
	}

}

type IdResp struct {
	Id int `json:"id"`
	ErrorMessage string `json:"err_msg"`
	Success bool `json:"success"`
}

func GetUserId(token string)(int, error){
	resp, err := _client.Request().AddPath("/user").AddQuery("token", token).Do()
	if err != nil {
		return -1, common.WarpWithBadReqErrorf(err, "Can not get user info, invalid token, may be expired")
	}
	idR := IdResp{}
	err = resp.JSON(&idR)
	if err != nil {
		return -1, common.WarpWithIntErrErrorf(err, "main server error, %s", err)
	}
	return idR.Id, nil
}


func GetPartyId(userId int)(int, error){
	resp, err := _client.Request().AddPath("/party").AddQuery("user_id", strconv.Itoa(userId)).Do()
	if err != nil {
		return -1, common.WarpWithBadReqErrorf(err, "Can not get user info, invalid token, may be expired")
	}
	idR := IdResp{}
	err = resp.JSON(&idR)
	if err != nil {
		return -1, common.WarpWithIntErrErrorf(err, "main server error, %s", err)
	}
	return idR.Id, nil
}

