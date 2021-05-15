package core

import (
	"context"
	"fmt"
	"github.com/gorilla/websocket"
	"github.com/pkg/errors"
	"github.com/resyon/InternetPlus/server/rtc/common"
)

type Client struct {
	Id    int
	Msg   chan *common.WsMsg
	//Voice chan []byte
	closed chan int
	Party *Party
}

func NewClient(clientId int)*Client{
	return &Client{
		Id:clientId,
		Msg: make(chan *common.WsMsg, 50),
		closed: make(chan int),
	}
}


func(c Client) Communicate(con *websocket.Conn)error{
	ctx := context.Background()
	errChan := make(chan error)
	//unexpectedSign := make(chan int)
	revCtx, cancel := context.WithCancel(ctx)

	con.SetCloseHandler(func(code int, text string) error {
		log := fmt.Sprintf("Connection close, return text = %s && code = %d", text, code)
		c.closed <- code
		switch code {
		case websocket.CloseGoingAway:
			common.Log.Infof(log)
		default:
			common.Log.Error(log)
		}
		return errors.New(log)
	})
	//msg rcv
	go func(ctx context.Context){
		//buf := make([]byte, 1024)
		for{
			select {
				case <-ctx.Done():
					return
			default:
				buf := &common.WsMsg{
				}
				err := con.ReadJSON(buf)
				buf.SenderId = c.Id
				if err != nil {
					common.Log.Error(err)
					errChan <- err
					break
				}
				c.Party.Broadcast <- buf
			}
		}
	}(revCtx)


	go func() {
		for{
			select {
			case sign := <- c.closed:
				cancel()
				errChan <- nil
				c.Party.UnSubChan <- c.Id

				err := errors.Errorf("catch an unexpected msg type %d", sign)
				common.Log.Error(err)


				errChan <- err
				return

			case t := <- c.Msg:
				err := con.WriteJSON(t)
				if err != nil {
					common.Log.Error(err)
					errChan <- err
				}

			}
		}
	}()

	return <-errChan
}
