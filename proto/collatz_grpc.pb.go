// Code generated by protoc-gen-go-grpc. DO NOT EDIT.
// versions:
// - protoc-gen-go-grpc v1.2.0
// - protoc             v3.19.4
// source: proto/collatz.proto

package proto

import (
	context "context"
	grpc "google.golang.org/grpc"
	codes "google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
)

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
// Requires gRPC-Go v1.32.0 or later.
const _ = grpc.SupportPackageIsVersion7

// CalculatorClient is the client API for Calculator service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://pkg.go.dev/google.golang.org/grpc/?tab=doc#ClientConn.NewStream.
type CalculatorClient interface {
	CalcCollatz(ctx context.Context, in *CollatzRequest, opts ...grpc.CallOption) (*CollatzResponse, error)
}

type calculatorClient struct {
	cc grpc.ClientConnInterface
}

func NewCalculatorClient(cc grpc.ClientConnInterface) CalculatorClient {
	return &calculatorClient{cc}
}

func (c *calculatorClient) CalcCollatz(ctx context.Context, in *CollatzRequest, opts ...grpc.CallOption) (*CollatzResponse, error) {
	out := new(CollatzResponse)
	err := c.cc.Invoke(ctx, "/collatz.Calculator/calcCollatz", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// CalculatorServer is the server API for Calculator service.
// All implementations must embed UnimplementedCalculatorServer
// for forward compatibility
type CalculatorServer interface {
	CalcCollatz(context.Context, *CollatzRequest) (*CollatzResponse, error)
	mustEmbedUnimplementedCalculatorServer()
}

// UnimplementedCalculatorServer must be embedded to have forward compatible implementations.
type UnimplementedCalculatorServer struct {
}

func (UnimplementedCalculatorServer) CalcCollatz(context.Context, *CollatzRequest) (*CollatzResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method CalcCollatz not implemented")
}
func (UnimplementedCalculatorServer) mustEmbedUnimplementedCalculatorServer() {}

// UnsafeCalculatorServer may be embedded to opt out of forward compatibility for this service.
// Use of this interface is not recommended, as added methods to CalculatorServer will
// result in compilation errors.
type UnsafeCalculatorServer interface {
	mustEmbedUnimplementedCalculatorServer()
}

func RegisterCalculatorServer(s grpc.ServiceRegistrar, srv CalculatorServer) {
	s.RegisterService(&Calculator_ServiceDesc, srv)
}

func _Calculator_CalcCollatz_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(CollatzRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(CalculatorServer).CalcCollatz(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/collatz.Calculator/calcCollatz",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(CalculatorServer).CalcCollatz(ctx, req.(*CollatzRequest))
	}
	return interceptor(ctx, in, info, handler)
}

// Calculator_ServiceDesc is the grpc.ServiceDesc for Calculator service.
// It's only intended for direct use with grpc.RegisterService,
// and not to be introspected or modified (even as a copy)
var Calculator_ServiceDesc = grpc.ServiceDesc{
	ServiceName: "collatz.Calculator",
	HandlerType: (*CalculatorServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "calcCollatz",
			Handler:    _Calculator_CalcCollatz_Handler,
		},
	},
	Streams:  []grpc.StreamDesc{},
	Metadata: "proto/collatz.proto",
}