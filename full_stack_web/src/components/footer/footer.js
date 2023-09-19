'use client'

import styles from './footer.module.css';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faDatabase, faUpRightFromSquare} from "@fortawesome/free-solid-svg-icons";

export function Footer() {
  return (
    <div className={styles.footer}>
      <div className={styles.copyright}>
        <a
          href={"https://github.com/QuadrangleKMLA/MedicineForAll.git"}
          target='_blank'
          className={styles.copyrightLink}
        >
          2023-2 Quadrangle - Jay Won Kim <FontAwesomeIcon icon={faUpRightFromSquare} />
        </a>
        {"    All Rights Reserved"}
      </div>
      <div className={styles.credits}>
        Developed with{" "}
        <a className={styles.creditsLink} href={"https://www.mysql.com/"}>
          <FontAwesomeIcon icon={faDatabase} />{"  "}MySQL
        </a>
      </div>
    </div>
  )
}