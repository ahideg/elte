let SayHello() = 
    printfn "Hello World"

[<EntryPoint>]
let main argv = 
    SayHello()
    System.Console.ReadKey(false) |> ignore
    0 
