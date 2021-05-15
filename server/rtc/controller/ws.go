package controller

import (
	"github.com/gin-gonic/gin"
	"github.com/gorilla/websocket"
	"github.com/resyon/InternetPlus/server/rtc/common"
	"github.com/resyon/InternetPlus/server/rtc/core"
	"github.com/resyon/InternetPlus/server/rtc/rpc"
)

var(
	ws = websocket.Upgrader{
		ReadBufferSize: 1024,
		WriteBufferSize: 1024,
	}
)
type WSCtrl struct {
}


type wsGetReq struct {
	Token string `json:"token"`
}

func(w *WSCtrl)Get(c *gin.Context){
	con, err := ws.Upgrade(c.Writer, c.Request, nil)
	if err != nil {
		common.ErrWs(err.Error(), c)
		return 
	}

	userId, err := common.GetUserId(c)
	if err != nil {
		con.WriteJSON(common.ErrWsResp(err.Error()))
		con.Close()
		return
	}
	client := core.NewClient(userId)
	partyId, err := rpc.GetPartyId(userId)
	if err != nil {
		con.WriteJSON(common.ErrWsResp(err.Error()))
		con.Close()
		return
	}
	party := core.NewParty(partyId)
	party.Register(client)
	err = client.Communicate(con)
	if err != nil {
		con.WriteJSON(common.ErrWsResp(err.Error()))
	}
	con.Close()
}
