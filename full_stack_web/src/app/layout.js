import './globals.css'
import { Inter } from 'next/font/google'
import { Navbar } from "@/components/navbar/navbar";
import { Footer } from "@/components/footer/footer";

const inter = Inter({ subsets: ['latin'] })

export const metadata = {
  title: 'Medicine For All',
  description: 'This app is still in development process!',
}

const navLinks = [{name: "Scan", href: "/"}, {name: 'Search', href: '/search'}, {name: 'Dashboard', href: '/dashboard'}]

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body className={inter.className}>
      <Navbar navLinks={navLinks} />
      {children}
      <Footer/>
      </body>
    </html>
  )
}
