using System.Net;
using System.Net.Sockets;
using System.Text;

namespace UDP
{
    public class UdpServer
    {
        private Socket _socket;
        private byte[] _buffer_reciv;
        private ArraySegment<byte> _buffer_reciv_segemnt;
        private IPEndPoint _endPoint;


        public void Initialize(int port)
        {
            _endPoint = new IPEndPoint(IPAddress.Any, port);

            _buffer_reciv = new byte[4096];
            _buffer_reciv_segemnt = new ArraySegment<byte>(_buffer_reciv);

            _socket = new(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp);
            // _socket.SetSocketOption(SocketOptionLevel.IP, SocketOptionName.PacketInformation, true);
            _socket.Bind(_endPoint);
        }


        public async Task StartMessageLoop()
        {
            Console.WriteLine($"UDP server started.");
            
            await Task.Run(async () =>
            {
                    SocketReceiveMessageFromResult res;
                    while (true)
                    {
                        
                        res = await _socket.ReceiveMessageFromAsync(_buffer_reciv_segemnt, SocketFlags.None, _endPoint);
                        Console.WriteLine($"Received data: {Encoding.UTF8.GetString(_buffer_reciv_segemnt)}");

                        SendTo(res.RemoteEndPoint, Encoding.UTF8.GetBytes("Hello U!!!"));

                        Console.WriteLine("Waiting for key down");
                        Console.Read();
                    }
                
            });
        }

        private async Task receive()
        {
        }

        public async void SendTo(EndPoint recipient, byte[] data)
        {
            var s = new ArraySegment<byte>(data);
            await _socket.SendToAsync(s, SocketFlags.None, recipient);
        }
    }


    public class UDPClient
    {
        private Socket _socket;
        private EndPoint _ep;

        private byte[] _buffer_recv;
        private ArraySegment<byte> _buffer_recv_segment;

        public void Initialize(IPAddress address, int port)
        {
            _buffer_recv = new byte[4096];
            _buffer_recv_segment = new(_buffer_recv);

            _ep = new IPEndPoint(address, port);

            _socket = new(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp);
            _socket.SetSocketOption(SocketOptionLevel.IP, SocketOptionName.PacketInformation, true);
        }

        public void StartMessageLoop()
        {
            _ = Task.Run(async () =>
            {
                SocketReceiveMessageFromResult res;
                while (true)
                {
                    res = await _socket.ReceiveMessageFromAsync(_buffer_recv_segment, SocketFlags.None, _ep);
                    Console.WriteLine(
                        $"Received data from remote {res.RemoteEndPoint}: {Encoding.UTF8.GetString(_buffer_recv_segment)}");
                }
            });
        }

        public async Task Send(byte[] data)
        {
            var s = new ArraySegment<byte>(data);
            await _socket.SendToAsync(s, SocketFlags.None, _ep);
        }
    }
}