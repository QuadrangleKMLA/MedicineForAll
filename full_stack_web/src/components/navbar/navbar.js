'use client'

import {usePathname} from "next/navigation";
import Link from "next/link";
import styles from './navbar.module.css';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {faCameraRetro, faClipboardUser, faMagnifyingGlass} from "@fortawesome/free-solid-svg-icons";

export function Navbar({ navLinks }) {
  const pathname = usePathname()
  
  return (
    <nav className={styles.navbar}>
      <div>
        <h1>
          <Link href={"/"} className={styles.title}>
            MED FOR ALL
          </Link>
        </h1>
      </div>
      <div className={styles.center}>
        {navLinks.map((link) => {
          const active = pathname === link.href
          
          return (
            <Link
              className={active ? styles.navLinksActive : styles.navLinksInactive}
              href={link.href}
              key={link.name}
            >
              {link.name === "Search" ? (<><FontAwesomeIcon icon={faMagnifyingGlass} className={styles.search} />{"  " + link.name}</>) : link.name === "Scan" ? (<><FontAwesomeIcon icon={faCameraRetro} />{"  " + link.name}</>) : link.name === "Dashboard" ? (<><FontAwesomeIcon icon={faClipboardUser} />{" " + link.name}</>) : link.name}
            </Link>
          )
        })}
      </div>
    </nav>
  )
}