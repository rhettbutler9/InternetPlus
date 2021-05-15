package middleware


import (
	"errors"
	"github.com/resyon/InternetPlus/server/rtc/common"
	"github.com/resyon/InternetPlus/server/rtc/rpc"
	"net/http"


	"github.com/gin-gonic/gin"
)



func Auth() gin.HandlerFunc {
	return func(c *gin.Context) {

		token := c.Query("token")
		if token == ""{
			c.JSON(http.StatusForbidden, common.NewBadR(errors.New("X-Token Not Found")))
			c.Abort()
			return
		}
		id, err := rpc.GetUserId(token)
		if err != nil {
			common.ErrWs(err.Error(), c)
			c.Abort()
			return
		}
		c.Set("uid", id)
		c.Next()
	}
}