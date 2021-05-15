package common

import (
	"github.com/gin-gonic/gin"
	"github.com/pkg/errors"
)

func newGin()*gin.Engine{
	server := gin.New()

	return server
}


func GetUserId(gin *gin.Context) (int, error){
	 uidI, exist := gin.Get("uid")
	 if ! exist {
	 	return -1, WarpWithBadReqErrorf(errors.New("invalid user_id"), "")
	 }
	 uid , ok := uidI.(int)
	 if !ok {
	 	return -1, WarpWithIntErrErrorf(errors.New("it should never happen, uid can not convert to int"), "")
	 }
	 return uid, nil
}
