import styles from './info.module.css'

export const metadata = {
  title: 'Information Page for Designated Medicine ID',
  description: 'This page shows the information of the medicine that is indicated by the {id} passed as a path variable.'
}

export default function Information() {
  return (
    <div className={styles.home}>
      <h1>Information!!!</h1>
    </div>
  )
}