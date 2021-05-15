package core

import (
	"context"
	"github.com/resyon/InternetPlus/server/rtc/common"
	"sync"
)

var (
	partis = newPartyLife()
)

type Party struct {
	Id            int

	Broadcast      chan *common.WsMsg
	//VoiceBroadcast chan []byte
	Clients        map[int]*Client

	UnSubChan chan int

	cancel context.CancelFunc

	mutex sync.RWMutex
}

//partyLife count the people in party, delete party while count down to 0
type partyLife struct {

	parties map[int]*Party
	sync.RWMutex
}

func newPartyLife()*partyLife{
	return &partyLife{
		parties: make(map[int]*Party),
	}
}

func (pl partyLife)AddRecord(id int, p *Party){
	pl.RWMutex.Lock()
	defer pl.RWMutex.Unlock()
	pl.parties[id] = p
}

func (pl partyLife)GetParty(id int)(*Party, bool){
	pl.RLock()
	defer pl.RUnlock()
	p, ok := pl.parties[id]
	return p, ok
}

func (pl partyLife)DeleteRecord(id int){
	pl.Lock()
	defer pl.Unlock()

	delete(pl.parties, id)

}

func NewParty(partyId int)*Party{
	if rtn, ok := partis.GetParty(partyId); ok {
		return rtn
	}

	rtn := &Party{
		Id:             partyId,
		Broadcast:      make(chan *common.WsMsg, 50),
		Clients: map[int]*Client{},
		//VoiceBroadcast: make(chan []byte),
	}
	ctx, cancel := context.WithCancel(context.Background())
	rtn.cancel = cancel
	go rtn.broadCastD(ctx)
	partis.AddRecord(partyId, rtn)
	return rtn
}


func(p *Party) Register(client *Client){
	p.mutex.Lock()
	defer p.mutex.Unlock()
	p.Clients[client.Id] = client
	client.Party = p
}

func(p *Party)UnRegister(clientId int){
	p.mutex.Lock()
	delete(p.Clients, clientId)
	if len(p.Clients) == 0 {
		p.cancel()
	}
	partis.DeleteRecord(p.Id)
	p.mutex.Unlock()
}


func (p *Party)broadCastD(ctx context.Context){
	for {
		select {
		case <-ctx.Done():
			//partis.DeleteRecord(p.Id)
			return
		case buf := <- p.Broadcast :
			p.mutex.RLock()
			for _, client := range p.Clients {
				client.Msg <- buf
			}
			p.mutex.RUnlock()
		}
	}
}




