// // See https://aka.ms/new-console-template for more information
//
// using System.Net;
// using System.Net.Sockets;
// using System.Text;
//
//
// AAA
// BBB
// CCC
//
// Task.Run(async () =>
// {
//     try
//     {
//         var udpClient = new UdpClient(4399);
//
//         while (true)
//         {
//             var res = await udpClient.ReceiveAsync();
//
//             Console.WriteLine($"Receive data: \"{Encoding.UTF8.GetString(res.Buffer)}\"");
//
//             await udpClient.SendAsync(Encoding.UTF8.GetBytes($"Server received\nYour address: {res.RemoteEndPoint}"), res.RemoteEndPoint);
//         }
//
//     }
//     catch (Exception e)
//     {
//         Console.WriteLine(e);
//         throw;
//     }
// });
//
// await Task.Run(async () =>
// {
//     var udpClient = new UdpClient();
//     udpClient.Connect(IPAddress.Any, 4399);
//     Task.Run(async () =>
//     {
//         try
//         {
//             while (true)
//             {
//                 var res = await udpClient.ReceiveAsync();
//             
//                 Console.WriteLine($"Server Response: \"{Encoding.UTF8.GetString(res.Buffer)}\"");
//             }
//         }
//         catch (Exception e)
//         {
//             Console.WriteLine(e);
//             throw;
//         }
//     });
//     try
//     {
//         await Task.Delay(5000);
//         while (true)
//         {
//             Console.WriteLine("Input your message down here:");
//             var str = Console.ReadLine();
//             if (str != null)
//             {
//                 await udpClient.SendAsync(Encoding.UTF8.GetBytes(str));
//             }
//         }
//     }
//     catch (Exception e)
//     {
//         Console.WriteLine(e);
//         throw;
//     }
// });
//
// // Task.Run(async () =>
// // {
// //     await Task.Delay(5000);
// //     var client = new UDPClient();
// //     client.Initialize(IPAddress.Parse("127.0.0.1"), 4399);
// //     await client.Send(Encoding.UTF8.GetBytes("Hello"));
// // });
// //
// // var server = new UdpServer();
// // server.Initialize(4399);
// // await server.StartMessageLoop();
//
//
// // client.StartMessageLoop();