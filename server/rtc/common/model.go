package common

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

type WebSocketResp struct {
	StatusCode uint16 `json:"status_code"`
	Data       []byte `json:"data"`
	Message string `json:"message"`
}

type WsMsg struct {
	//StatusCode uint16 `json:"status_code"`
	Data       []byte `json:"data"`
	Message string `json:"message"`
	SenderId int `json:"sender_id"`
	IsText bool `json:"is_text"`
}

func OkWsResp(data []byte)WebSocketResp{
	return WebSocketResp{
		StatusCode: 200,
		Data:       data,
	}
}

func ErrWsResp(errMsg string)WebSocketResp{
	Log.Error(errMsg)
	return WebSocketResp{
		StatusCode: http.StatusInternalServerError,
		Message:       errMsg,
	}
}

func (wsp WebSocketResp)ToByteArr()[]byte{
	rtn := []byte{
		byte(wsp.StatusCode & 0x00ff),
		byte(wsp.StatusCode >> 8),
	}
	return append(rtn, wsp.Data...)
}

func ErrWs(errMsg string, c *gin.Context){
	Log.Error(errMsg)
	c.Writer.Write(ErrWsResp(errMsg).ToByteArr())
}


