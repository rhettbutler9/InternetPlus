package main

import (
	"github.com/resyon/InternetPlus/server/rtc/common"
	"github.com/resyon/InternetPlus/server/rtc/controller"
	"github.com/resyon/InternetPlus/server/rtc/middleware"
)

func main() {

	server := common.Gin

	server.Use(middleware.Auth())
	//ws
	ws := server.Group("/api/v1/rtc/ws")
	{
		wsCtrl := controller.WSCtrl{}
		ws.GET("/msg", wsCtrl.Get)
	}
	server.Run(":51707")
}
