package common

import "github.com/pkg/errors"

type BadReqError struct {
	Cause error
}

func(err BadReqError)Error()string{
	return err.Cause.Error()
}

func WarpWithBadReqErrorf(err error, fmt string, obj ...interface{})BadReqError{
	return 	BadReqError{
		errors.WithMessagef(err, fmt, obj...),
	}
}

func IsBadReqError(err error)bool{
	_, ok := err.(BadReqError)
	return ok
}

type IntErrError struct {
	Cause error
}

func WarpWithIntErrErrorf(err error, fmt string, obj ...interface{})IntErrError{
	return 	IntErrError{
		errors.WithMessagef(err, fmt, obj...),
	}
}
func(err IntErrError)Error()string{
	return err.Cause.Error()
}

func IsIntErrError(err error)bool{
	_, ok := err.(IntErrError)
	return ok
}
