use tonic::{transport::Server, Request, Response, Status};

use collatz::calculator_server::{Calculator, CalculatorServer};
use collatz::{CollatzResponse, CollatzRequest};

pub mod collatz {
    tonic::include_proto!("collatz");
}

#[derive(Debug, Default)]
pub struct MyCalculator {}

fn collatz(n: i64) -> i64 {
    let mut count = 1;
    let mut m: i64 = n;
    while m > 1 {
        if m % 2 == 0 {
            m = m / 2;
        } else {
            m = m * 3 + 1;
        }
        count += 1;
    }
    return count;
}

fn main_loop(s: i64, e: i64) -> (i64, i64) {
    let mut max: i64 = 0;
    let mut key: i64 = 0;
    for i in s..e {
        let rc = collatz(i);
        if max < rc {
            max = rc;
            key = i;
        }
    }
    return (key, max)
}

#[tonic::async_trait]
impl Calculator for MyCalculator {
    async fn calc_collatz(
        &self,
        request: Request<CollatzRequest>,
    ) -> Result<Response<CollatzResponse>, Status> {
        println!("Got a request: {:?}", request);

        let req = &request.into_inner();
        let s = req.index_from;
        let e = req.index_to;
        let result = main_loop(s, e);
        let response = collatz::CollatzResponse {
            max_length: result.1,
            index: result.0
        };

        Ok(Response::new(response))
    }
}

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    let addr = "[::1]:50051".parse()?;
    let collatz = MyCalculator::default();

    Server::builder()
        .add_service(CalculatorServer::new(collatz))
        .serve(addr)
        .await?;

    Ok(())
}