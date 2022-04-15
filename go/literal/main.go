package main

import (
	"context"
	"fmt"
	"log"
	"net"
	"time"

	pb "github.com/ebagos/collatz/proto"
	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

func collatz(n int64) int64 {
	var count int64 = 1
	m := n
	for m > 1 {
		if m%2 == 0 {
			m /= 2
		} else {
			m = m*3 + 1
		}
		count++
	}
	return count
}

type server struct {
	pb.UnimplementedCalculatorServer
}

func mainLoop(s, e int64) (int64, int64) {
	now := time.Now()
	var max int64 = 0
	var key int64 = 0
	for i := s; i < e; i++ {
		rc := collatz(i)
		if rc > max {
			max = rc
			key = i
		}
	}
	fmt.Printf("%d(%d)\n", key, max)
	fmt.Printf("%v ms\n", time.Since(now).Milliseconds())
	return key, max
}

func main() {
	listenPort, err := net.Listen("tcp", ":50051")
	defer listenPort.Close()
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}
	grpcServer := grpc.NewServer()
	pb.RegisterCalculatorServer(grpcServer, &server{})
	if err := grpcServer.Serve(listenPort); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}
}

func (s *server) CalcCollatz(ctx context.Context, in *pb.CollatzRequest) (*pb.CollatzResponse, error) {
	st := in.IndexFrom
	ed := in.IndexTo
	if st > ed || st < 2 || ed > 1000000000 {
		return nil, status.Error(codes.InvalidArgument, "Invalid prameter(s)")
	}
	index, max := mainLoop(st, ed)
	return &pb.CollatzResponse{
		MaxLength: max,
		Index:     index,
	}, nil
}
