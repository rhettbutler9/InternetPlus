package main

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"time"
)

var (
	testGin = gin.New()
)

type IdResp struct {
	Id int `json:"id"`
	ErrorMessage string `json:"err_msg"`
	Success bool `json:"success"`
}
func main(){
	t := testGin.Group("/api/v1")
	{
		tc := testCtrl{}
		t.GET("/login", tc.Login)
		t.GET("/user", tc.Auth)
		t.GET("/party", tc.Party)
	}
	testGin.Run(":9000")
}

type testCtrl struct {

}

func(t testCtrl)Login(c *gin.Context){
	c.JSON(http.StatusOK, IdResp{
		Success: true,
	})
}
func(t testCtrl) Auth(c *gin.Context){
	token := c.Query("token")
	if token != "" {
		c.JSON(200, IdResp{
			Id: int(time.Now().Unix()),
			Success: true,
		})
		return
	}
	c.JSON(http.StatusBadRequest, IdResp{
		Success: false,
		ErrorMessage: "invalid token",
	})
}

func(t testCtrl) Party(c *gin.Context){
	id := c.Query("user_id")
	if id != "" {
		c.JSON(200, IdResp{
			Id: time.Now().Day(),
			Success: true,
		})
		return
	}
	c.JSON(http.StatusBadRequest, IdResp{
		Success: false,
		ErrorMessage: "invalid token",
	})
}

