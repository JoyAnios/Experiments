using System.Net;
using System.Net.Sockets;
using System.Text;


await Task.Run(async () =>
{
    var client = new UdpClient();
    var iep = new IPEndPoint(IPAddress.Any, 4399);
    client.EnableBroadcast = true;

    try
    {
        client.Connect(iep);
        Console.WriteLine("Listening");

        while (true)
        {
            var receiveResult = await client.ReceiveAsync();
            Console.WriteLine(Encoding.ASCII.GetString(receiveResult.Buffer));
            await client.SendAsync(Encoding.ASCII.GetBytes("DSUS"));
        }
    }
    catch (Exception e)
    {
        Console.WriteLine(e);
    }
    finally
    {
        client.Dispose();
    }
});