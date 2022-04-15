use collatz::calculator_client::CalculatorClient;
use collatz::CollatzRequest;

pub mod collatz {
    tonic::include_proto!("collatz");
}

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    let mut client = CalculatorClient::connect("http://[::1]:50051").await?;

    let request = tonic::Request::new(CollatzRequest {
        index_from: 1.into(),
        index_to: 1000.into()
    });

    let response = client.calc_collatz(request).await?;

    println!("RESPONSE={:?}", response);

    Ok(())
}