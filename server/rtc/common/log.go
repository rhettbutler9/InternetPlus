package common

import (
	"go.uber.org/zap"
)

func newLog()*zap.SugaredLogger{
	logger, err := zap.NewProduction()
	if err != nil {
		panic(err)
	}
	defer logger.Sync() // flushes buffer, if any
	return logger.Sugar()
}
